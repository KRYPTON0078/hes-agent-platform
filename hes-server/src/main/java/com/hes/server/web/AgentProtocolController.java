package com.hes.server.web;

import com.hes.common.error.ErrorCode;
import com.hes.common.protocol.AgentMessage;
import com.hes.common.protocol.MessageType;
import com.hes.server.security.AgentAuthInterceptor;
import com.hes.server.service.CommandService;
import com.hes.server.service.DeviceRegistryService;
import com.hes.server.service.TelemetryIngestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agent")
@Tag(name = "Agent Protocol")
public class AgentProtocolController {

    private final DeviceRegistryService deviceRegistryService;
    private final TelemetryIngestService telemetryIngestService;
    private final CommandService commandService;

    public AgentProtocolController(DeviceRegistryService deviceRegistryService,
                                   TelemetryIngestService telemetryIngestService,
                                   CommandService commandService) {
        this.deviceRegistryService = deviceRegistryService;
        this.telemetryIngestService = telemetryIngestService;
        this.commandService = commandService;
    }

    @PostMapping("/messages")
    @Operation(summary = "Accept an Agent protocol v1 message")
    public AgentMessage ingest(@Valid @RequestBody AgentMessage message, HttpServletRequest request) {
        if (message.type() != MessageType.AGENT_REGISTER) {
            String apiKey = request.getHeader(AgentAuthInterceptor.API_KEY_HEADER);
            deviceRegistryService.assertApiKey(message.deviceId(), apiKey);
        }
        return switch (message.type()) {
            case AGENT_REGISTER -> deviceRegistryService.register(message);
            case HEARTBEAT -> deviceRegistryService.heartbeat(message);
            case TELEMETRY_REPORT -> telemetryIngestService.accept(message);
            case COMMAND_ACK -> commandService.acknowledge(message);
            default -> throw new BusinessException(
                    ErrorCode.PROTOCOL_UNSUPPORTED,
                    "Unsupported inbound type: " + message.type()
            );
        };
    }

    @GetMapping("/{deviceId}/commands")
    @Operation(summary = "Agent polls pending COMMAND_DISPATCH messages")
    public List<AgentMessage> pollCommands(@PathVariable String deviceId) {
        return commandService.pollCommands(deviceId);
    }
}
