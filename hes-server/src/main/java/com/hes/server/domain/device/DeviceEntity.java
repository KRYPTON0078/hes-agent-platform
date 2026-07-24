package com.hes.server.domain.device;

import com.hes.server.domain.site.SiteEntity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "device", indexes = {
        @Index(name = "uk_device_id", columnList = "device_id", unique = true),
        @Index(name = "idx_device_status_updated", columnList = "status, updated_at")
})
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false, length = 64)
    private String deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private SiteEntity site;

    @Column(nullable = false, length = 64)
    private String model;

    @Column(name = "firmware_version", length = 32)
    private String firmwareVersion;

    @Column(length = 256)
    private String tags;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private DeviceStatus status = DeviceStatus.REGISTERED;

    @Column(name = "last_seen_at")
    private Instant lastSeenAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public SiteEntity getSite() { return site; }
    public void setSite(SiteEntity site) { this.site = site; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public DeviceStatus getStatus() { return status; }
    public void setStatus(DeviceStatus status) { this.status = status; }
    public Instant getLastSeenAt() { return lastSeenAt; }
    public void setLastSeenAt(Instant lastSeenAt) { this.lastSeenAt = lastSeenAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
