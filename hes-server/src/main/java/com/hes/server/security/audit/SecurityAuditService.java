package com.hes.server.security.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Append-only security audit writer. No update/delete APIs by design.
 */
@Service
public class SecurityAuditService {

    private final SecurityAuditEventRepository repository;
    private final ObjectMapper objectMapper;

    public SecurityAuditService(SecurityAuditEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void record(String eventType, String actor, String subject, Map<String, Object> detail, String ip) {
        SecurityAuditEventEntity event = new SecurityAuditEventEntity();
        event.setEventType(eventType);
        event.setActor(actor);
        event.setSubject(subject);
        event.setIpAddress(ip);
        try {
            event.setDetailJson(objectMapper.writeValueAsString(detail == null ? Map.of() : detail));
        } catch (JsonProcessingException e) {
            event.setDetailJson("{}");
        }
        repository.save(event);
    }

    @Transactional(readOnly = true)
    public List<SecurityAuditEventEntity> latest() {
        return repository.findTop100ByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<SecurityAuditEventEntity> latestByType(String type) {
        return repository.findTop100ByEventTypeOrderByCreatedAtDesc(type);
    }
}
