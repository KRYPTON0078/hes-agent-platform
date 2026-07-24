package com.hes.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hes.common.error.ErrorCode;
import com.hes.common.protocol.AgentMessage;
import com.hes.common.protocol.CommandType;
import com.hes.common.protocol.MessageType;
import com.hes.server.cache.InMemoryCommandIdempotencyStore;
import com.hes.server.config.HesProperties;
import com.hes.server.domain.command.*;
import com.hes.server.domain.device.DeviceEntity;
import com.hes.server.messaging.InProcessAgentEventBus;
import com.hes.server.messaging.PendingCommandBuffer;
import com.hes.server.presence.InMemoryOnlinePresenceStore;
import com.hes.server.web.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandServiceTest {

    @Mock CommandRepository commandRepository;
    @Mock CommandEventRepository commandEventRepository;
    @Mock DeviceRegistryService deviceRegistryService;

    PendingCommandBuffer buffer;
    CommandService service;
    DeviceEntity device;

    @BeforeEach
    void setUp() {
        HesProperties properties = new HesProperties();
        buffer = new PendingCommandBuffer();
        InMemoryOnlinePresenceStore presence = new InMemoryOnlinePresenceStore(properties);
        presence.heartbeat("HES-1", Instant.now());
        device = new DeviceEntity();
        device.setDeviceId("HES-1");

        service = new CommandService(
                commandRepository,
                commandEventRepository,
                deviceRegistryService,
                new InProcessAgentEventBus(mock(TelemetryIngestService.class), buffer),
                buffer,
                presence,
                new InMemoryCommandIdempotencyStore(),
                properties,
                new ObjectMapper()
        );

        lenient().when(deviceRegistryService.requireDevice("HES-1")).thenReturn(device);
        lenient().when(commandRepository.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        lenient().when(commandRepository.save(any(CommandEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(commandRepository.saveAndFlush(any(CommandEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(commandEventRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void issueDispatchesAndBuffersCommand() {
        CommandEntity issued = service.issue("HES-1", CommandType.START_CHARGE, Map.of("watts", 1000), "idem-1", "ops");
        assertEquals(CommandStatus.DISPATCHED, issued.getStatus());
        assertEquals(1, buffer.drain("HES-1").size());
    }

    @Test
    void acknowledgeIgnoresTerminalCommands() {
        CommandEntity existing = new CommandEntity();
        existing.setCommandId("c1");
        existing.setDevice(device);
        existing.setStatus(CommandStatus.TIMEOUT);
        when(commandRepository.findByCommandId("c1")).thenReturn(Optional.of(existing));

        AgentMessage ack = service.acknowledge(AgentMessage.of(
                MessageType.COMMAND_ACK,
                UUID.randomUUID().toString(),
                "HES-1",
                Map.of("commandId", "c1", "success", true)
        ));
        assertEquals("ALREADY_TERMINAL", ack.payload().get("status"));
        verify(commandRepository, never()).save(existing);
    }

    @Test
    void acknowledgeRequiresCommandId() {
        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.acknowledge(AgentMessage.of(
                        MessageType.COMMAND_ACK,
                        UUID.randomUUID().toString(),
                        "HES-1",
                        Map.of("success", true)
                )));
        assertEquals(ErrorCode.VALIDATION_FAILED, ex.getCode());
    }
}
