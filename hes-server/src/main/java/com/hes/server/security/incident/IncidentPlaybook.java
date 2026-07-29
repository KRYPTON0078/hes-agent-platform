package com.hes.server.security.incident;

import java.util.List;

public interface IncidentPlaybook {
    String id();
    String title();
    List<String> steps();
    String severity();
}
