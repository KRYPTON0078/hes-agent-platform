package com.hes.server.domain.command;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "command_event", indexes = {
        @Index(name = "idx_command_event_command", columnList = "command_id, created_at")
})
public class CommandEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "command_id", nullable = false)
    private CommandEntity command;

    @Column(name = "event_type", nullable = false, length = 32)
    private String eventType;

    @Lob
    @Column(name = "detail_json", columnDefinition = "TEXT")
    private String detailJson;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public CommandEntity getCommand() { return command; }
    public void setCommand(CommandEntity command) { this.command = command; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getDetailJson() { return detailJson; }
    public void setDetailJson(String detailJson) { this.detailJson = detailJson; }
    public Instant getCreatedAt() { return createdAt; }
}
