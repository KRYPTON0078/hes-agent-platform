package com.hes.server.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/v1/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "status", "UP",
                "service", "hes-server",
                "protocol", "agent-v1"
        );
    }
}
