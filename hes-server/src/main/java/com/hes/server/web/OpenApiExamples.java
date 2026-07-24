package com.hes.server.web;

/** Shared OpenAPI narrative examples for Swagger UI. */
public final class OpenApiExamples {
    public static final String TELEMETRY_JSON = """
            {
              "protocolVersion": "1.0",
              "type": "TELEMETRY_REPORT",
              "messageId": "11111111-1111-1111-1111-111111111111",
              "deviceId": "HES-SIM-001",
              "payload": {
                "socPercent": 48.2,
                "batteryKwh": 4.82,
                "inverterWatts": 1500,
                "operatingMode": "DISCHARGING"
              }
            }
            """;

    private OpenApiExamples() {}
}
