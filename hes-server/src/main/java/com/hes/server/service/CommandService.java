package com.hes.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hes.common.error.ErrorCode;
import com.hes.common.protocol.AgentMessage;
import com.hes.common.protocol.CommandType;
import com.hes.common.protocol.MessageType;
import com.hes.server.cache.CommandIdempotencyStore;
import com.hes.server.config.HesProperties;
import com.hes.server.domain.command.*;
import com.hes.server.domain.device.DeviceEntity;
import com.hes.server.messaging.AgentEventBus;
import com.hes.server.messaging.PendingCommandBuffer;
import com.hes.server.presence.OnlinePresenceStore;
import com.hes.server.web.BusinessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CommandService {

    private static final EnumSet<CommandStatus> TERMINAL = EnumSet.of(
            CommandStatus.ACKED, CommandStatus.FAILED, CommandStatus.TIMEOUT
    );

    private final CommandRepository commandRepository;
    private final CommandEventRepository commandEventRepository;
    private final DeviceRegistryService deviceRegistryService;
    private final AgentEventBus eventBus;
    private final PendingCommandBuffer pendingCommandBuffer;
    private final OnlinePresenceStore presenceStore;
    private final CommandIdempotencyStore idempotencyStore;
    private final HesProperties properties;
    private final ObjectMapper objectMapper;

    public CommandService(CommandRepository commandRepository,
                          CommandEventRepository commandEventRepository,
                          DeviceRegistryService deviceRegistryService,
                          AgentEventBus eventBus,
                          PendingCommandBuffer pendingCommandBuffer,
                          OnlinePresenceStore presenceStore,
                          CommandIdempotencyStore idempotencyStore,
                          HesProperties properties,
                          ObjectMapper objectMapper) {
        this.commandRepository = commandRepository;
        this.commandEventRepository = commandEventRepository;
        this.deviceRegistryService = deviceRegistryService;
        this.eventBus = eventBus;
        this.pendingCommandBuffer = pendingCommandBuffer;
        this.presenceStore = presenceStore;
        this.idempotencyStore = idempotencyStore;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public CommandEntity issue(String deviceId, CommandType type, Map<String, Object> payload,
                               String idempotencyKey, String requestedBy) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "idempotencyKey is required");
        }
        return commandRepository.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> createAndDispatch(deviceId, type, payload, idempotencyKey, requestedBy));
    }

    private CommandEntity createAndDispatch(String deviceId, CommandType type, Map<String, Object> payload,
                                            String idempotencyKey, String requestedBy) {
        DeviceEntity device = deviceRegistryService.requireDevice(deviceId);
        if (!presenceStore.isOnline(deviceId)) {
            throw new BusinessException(ErrorCode.DEVICE_OFFLINE, "Device is offline: " + deviceId);
        }

        String commandId = UUID.randomUUID().toString();
        if (!idempotencyStore.tryClaim(idempotencyKey, commandId)) {
            return commandRepository.findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.IDEMPOTENCY_CONFLICT,
                            "Idempotency key already claimed: " + idempotencyKey
                    ));
        }

        CommandEntity command = new CommandEntity();
        command.setCommandId(commandId);
        command.setDevice(device);
        command.setCommandType(type);
        command.setPayloadJson(toJson(payload == null ? Map.of() : payload));
        command.setStatus(CommandStatus.PENDING);
        command.setIdempotencyKey(idempotencyKey);
        command.setRequestedBy(requestedBy == null ? "ops" : requestedBy);
        command.setTimeoutAt(Instant.now().plusSeconds(properties.getAgent().getCommandTimeoutSeconds()));

        try {
            command = commandRepository.saveAndFlush(command);
        } catch (DataIntegrityViolationException ex) {
            return commandRepository.findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.IDEMPOTENCY_CONFLICT,
                            "Idempotency key conflict: " + idempotencyKey
                    ));
        }

        appendEvent(command, "CREATED", Map.of("requestedBy", command.getRequestedBy()));

        Map<String, Object> dispatchPayload = new HashMap<>();
        dispatchPayload.put("commandId", command.getCommandId());
        dispatchPayload.put("commandType", type.name());
        dispatchPayload.put("params", payload == null ? Map.of() : payload);

        AgentMessage dispatch = AgentMessage.of(
                MessageType.COMMAND_DISPATCH,
                command.getCommandId(),
                deviceId,
                dispatchPayload
        );
        eventBus.publishCommand(dispatch);
        command.setStatus(CommandStatus.DISPATCHED);
        appendEvent(command, "DISPATCHED", Map.of());
        return commandRepository.save(command);
    }

    @Transactional
    public AgentMessage acknowledge(AgentMessage message) {
        if (message.type() != MessageType.COMMAND_ACK) {
            throw new BusinessException(ErrorCode.PROTOCOL_UNSUPPORTED, "Expected COMMAND_ACK");
        }
        Map<String, Object> payload = message.payload() == null ? Map.of() : message.payload();
        Object rawCommandId = payload.get("commandId");
        if (rawCommandId == null || String.valueOf(rawCommandId).isBlank() || "null".equals(String.valueOf(rawCommandId))) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "commandId is required");
        }
        String commandId = String.valueOf(rawCommandId);
        CommandEntity command = commandRepository.findByCommandId(commandId)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_FAILED, "Unknown commandId"));

        if (TERMINAL.contains(command.getStatus())) {
            return AgentMessage.of(
                    MessageType.COMMAND_ACK,
                    UUID.randomUUID().toString(),
                    message.deviceId(),
                    Map.of("status", "ALREADY_TERMINAL", "commandId", commandId, "currentStatus", command.getStatus().name())
            );
        }

        boolean success = Boolean.parseBoolean(String.valueOf(payload.getOrDefault("success", "true")));
        command.setStatus(success ? CommandStatus.ACKED : CommandStatus.FAILED);
        appendEvent(command, success ? "ACKED" : "FAILED", payload);
        commandRepository.save(command);
        return AgentMessage.of(
                MessageType.COMMAND_ACK,
                UUID.randomUUID().toString(),
                message.deviceId(),
                Map.of("status", "RECORDED", "commandId", commandId)
        );
    }

    public List<AgentMessage> pollCommands(String deviceId) {
        deviceRegistryService.requireDevice(deviceId);
        return pendingCommandBuffer.drain(deviceId);
    }

    @Transactional
    public int markTimeouts() {
        List<CommandEntity> timedOut = commandRepository.findByStatusAndTimeoutAtBefore(
                CommandStatus.DISPATCHED, Instant.now());
        // Also sweep PENDING stuck past timeout (failed mid-dispatch)
        List<CommandEntity> pendingTimedOut = commandRepository.findByStatusAndTimeoutAtBefore(
                CommandStatus.PENDING, Instant.now());
        timedOut = new java.util.ArrayList<>(timedOut);
        timedOut.addAll(pendingTimedOut);
        for (CommandEntity command : timedOut) {
            command.setStatus(CommandStatus.TIMEOUT);
            appendEvent(command, "TIMEOUT", Map.of());
        }
        commandRepository.saveAll(timedOut);
        return timedOut.size();
    }

    public CommandEntity get(String commandId) {
        return commandRepository.findByCommandId(commandId)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_FAILED, "Command not found"));
    }

    private void appendEvent(CommandEntity command, String type, Map<String, Object> detail) {
        CommandEventEntity event = new CommandEventEntity();
        event.setCommand(command);
        event.setEventType(type);
        event.setDetailJson(toJson(detail));
        commandEventRepository.save(event);
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
