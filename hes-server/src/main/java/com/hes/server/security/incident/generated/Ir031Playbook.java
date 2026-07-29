package com.hes.server.security.incident.generated;

import com.hes.server.security.incident.IncidentPlaybook;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Ir031Playbook implements IncidentPlaybook {
    @Override public String id() { return "IR-031"; }
    @Override public String title() { return "Respond to security incident variant 31"; }
    @Override public String severity() { return "CRITICAL"; }
    @Override
    public List<String> steps() {
        return List.of(
            "Confirm alert IR-031 authenticity from audit trail",
            "Quarantine affected device if command abuse suspected",
            "Rotate Agent API keys for impacted devices",
            "Export forensic audit window and notify ADMIN"
        );
    }
}