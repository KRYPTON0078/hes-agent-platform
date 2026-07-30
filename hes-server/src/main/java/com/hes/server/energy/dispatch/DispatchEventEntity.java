package com.hes.server.energy.dispatch;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dispatch_event")
public class DispatchEventEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id", nullable = false, unique = true, length = 64) private String eventId;
    @Column(name = "device_id", nullable = false, length = 64) private String deviceId;
    @Column(nullable = false, length = 64) private String intent;
    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT") private String payloadJson;
    @Column(nullable = false) private boolean published;
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }
    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }
}