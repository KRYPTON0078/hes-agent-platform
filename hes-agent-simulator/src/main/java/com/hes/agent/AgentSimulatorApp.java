package com.hes.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hes.common.protocol.AgentMessage;
import com.hes.common.protocol.CommandType;
import com.hes.common.protocol.MessageType;
import com.hes.common.protocol.TelemetryPayload;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Simulated residential energy-storage Agent.
 * Speaks protocol v1 over HTTP: register → heartbeat/telemetry loop → command poll/ACK.
 */
public final class AgentSimulatorApp {

    private final String deviceId;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    private String apiKey;
    private BigDecimal soc = BigDecimal.valueOf(62.5);
    private String mode = "IDLE";
    private final LocalScheduleEvaluator scheduleEvaluator = new LocalScheduleEvaluator(1020, 1260, java.math.BigDecimal.valueOf(20), java.math.BigDecimal.valueOf(95));

    public AgentSimulatorApp(String deviceId, String baseUrl) {
        this.deviceId = deviceId;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public static void main(String[] args) throws Exception {
        String deviceId = args.length > 0 ? args[0] : "HES-SIM-001";
        String baseUrl = args.length > 1 ? args[1] : "http://localhost:8080";
        new AgentSimulatorApp(deviceId, baseUrl).run();
    }

    public void run() throws Exception {
        System.out.printf("Starting Agent simulator deviceId=%s baseUrl=%s%n", deviceId, baseUrl);
        AgentMessage ack = post(AgentMessage.of(
                MessageType.AGENT_REGISTER,
                UUID.randomUUID().toString(),
                deviceId,
                Map.of(
                        "model", "HES-BAT-10K",
                        "firmwareVersion", "1.2.0",
                        "siteCode", "SITE-DEMO",
                        "siteName", "Demo Household"
                )
        ), false);
        this.apiKey = String.valueOf(ack.payload().get("apiKey"));
        System.out.println("Registered: " + ack.type() + " apiKey=" + apiKey.substring(0, Math.min(8, apiKey.length())) + "...");

        while (true) {
            mode = scheduleEvaluator.evaluate(java.time.LocalTime.now(), soc).name();
            post(AgentMessage.of(MessageType.HEARTBEAT, UUID.randomUUID().toString(), deviceId, Map.of("uptimeSec", 1)), true);
            evolveTelemetry();
            TelemetryPayload telemetry = currentTelemetry();
            post(AgentMessage.of(
                    MessageType.TELEMETRY_REPORT,
                    UUID.randomUUID().toString(),
                    deviceId,
                    mapper.convertValue(telemetry, Map.class)
            ), true);
            pollAndAckCommands();
            Thread.sleep(5_000L);
        }
    }

    private void evolveTelemetry() {
        double delta = ThreadLocalRandom.current().nextDouble(-1.5, 1.5);
        if ("CHARGING".equals(mode)) {
            delta = Math.abs(delta) + 0.4;
        } else if ("DISCHARGING".equals(mode)) {
            delta = -Math.abs(delta) - 0.4;
        }
        soc = soc.add(BigDecimal.valueOf(delta)).max(BigDecimal.ONE).min(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private TelemetryPayload currentTelemetry() {
        BigDecimal inverter = "IDLE".equals(mode) ? BigDecimal.ZERO
                : BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(500, 3500)).setScale(2, RoundingMode.HALF_UP);
        return new TelemetryPayload(
                soc,
                soc.multiply(BigDecimal.TEN).divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP),
                inverter,
                inverter.negate(),
                BigDecimal.valueOf(1200),
                BigDecimal.valueOf(51.2),
                inverter.divide(BigDecimal.valueOf(51.2), 3, RoundingMode.HALF_UP),
                0,
                null,
                mode
        );
    }

    private void pollAndAckCommands() throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/v1/agent/" + deviceId + "/commands"))
                .timeout(Duration.ofSeconds(10))
                .GET();
        if (apiKey != null) {
            builder.header("X-Api-Key", apiKey);
        }
        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            System.err.println("Command poll failed: " + response.statusCode() + " " + response.body());
            return;
        }
        List<AgentMessage> commands = mapper.readValue(
                response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, AgentMessage.class)
        );
        for (AgentMessage command : commands) {
            applyCommand(command);
            Map<String, Object> payload = new HashMap<>();
            payload.put("commandId", command.payload().get("commandId"));
            payload.put("success", true);
            payload.put("appliedMode", mode);
            post(AgentMessage.of(MessageType.COMMAND_ACK, UUID.randomUUID().toString(), deviceId, payload), true);
            System.out.println("ACK command " + command.payload().get("commandId"));
        }
    }

    private void applyCommand(AgentMessage command) {
        Object type = command.payload().get("commandType");
        if (type == null) {
            return;
        }
        CommandType commandType = CommandType.valueOf(String.valueOf(type));
        switch (commandType) {
            case START_CHARGE -> mode = "CHARGING";
            case STOP_CHARGE -> mode = "IDLE";
            case SET_DISCHARGE_POWER -> mode = "DISCHARGING";
            case SET_CHARGE_POWER -> mode = "CHARGING";
            default -> {
            }
        }
    }

    private AgentMessage post(AgentMessage message, boolean authenticated) throws Exception {
        String json = mapper.writeValueAsString(message);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/v1/agent/messages"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));
        if (authenticated && apiKey != null) {
            builder.header("X-Api-Key", apiKey);
        }
        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IllegalStateException("Agent call failed: " + response.statusCode() + " " + response.body());
        }
        return mapper.readValue(response.body(), AgentMessage.class);
    }
}
