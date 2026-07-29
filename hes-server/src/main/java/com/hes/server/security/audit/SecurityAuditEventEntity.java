package com.hes.server.security.audit;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "security_audit_event")
public class SecurityAuditEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @Column(length = 128)
    private String actor;

    @Column(length = 128)
    private String subject;

    @Lob
    @Column(name = "detail_json")
    private String detailJson;

    @Column(name = "ip_address", length = 64)
    private String ipAddress;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDetailJson() { return detailJson; }
    public void setDetailJson(String detailJson) { this.detailJson = detailJson; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public Instant getCreatedAt() { return createdAt; }
}
