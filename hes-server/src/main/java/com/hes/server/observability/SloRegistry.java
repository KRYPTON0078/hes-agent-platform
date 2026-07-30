package com.hes.server.observability;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SloRegistry {
    public List<SloDefinition> definitions() {
        return List.of(
                new SloDefinition("slo-register", CriticalPath.AGENT_REGISTER, 0.995, 800),
                new SloDefinition("slo-telemetry", CriticalPath.AGENT_TELEMETRY, 0.99, 500),
                new SloDefinition("slo-command-ack", CriticalPath.COMMAND_ACK, 0.995, 700),
                new SloDefinition("slo-ops-auth", CriticalPath.OPS_AUTH, 0.999, 300),
                new SloDefinition("slo-schedule", CriticalPath.SCHEDULE_EVAL, 0.99, 400),
                new SloDefinition("slo-dispatch", CriticalPath.DISPATCH_EVAL, 0.99, 600)
        );
    }
}