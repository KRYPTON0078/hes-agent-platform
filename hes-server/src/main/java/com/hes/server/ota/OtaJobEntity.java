package com.hes.server.ota;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ota_job")
public class OtaJobEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "job_code", nullable = false, unique = true, length = 64) private String jobCode;
    @Column(name = "device_id", nullable = false, length = 64) private String deviceId;
    @Column(name = "firmware_version", nullable = false, length = 64) private String firmwareVersion;
    @Column(name = "package_url", nullable = false, length = 512) private String packageUrl;
    @Column(name = "package_sha256", nullable = false, length = 64) private String packageSha256;
    @Column(nullable = false, length = 32) private String phase;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();

    public Long getId() { return id; }
    public String getJobCode() { return jobCode; }
    public void setJobCode(String jobCode) { this.jobCode = jobCode; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public String getPackageUrl() { return packageUrl; }
    public void setPackageUrl(String packageUrl) { this.packageUrl = packageUrl; }
    public String getPackageSha256() { return packageSha256; }
    public void setPackageSha256(String packageSha256) { this.packageSha256 = packageSha256; }
    public String getPhase() { return phase; }
    public void setPhase(String phase) { this.phase = phase; this.updatedAt = Instant.now(); }
}