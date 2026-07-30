package com.hes.server.security.enrollment;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "enrollment_token")
public class EnrollmentTokenEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token_hash", nullable = false, unique = true, length = 128) private String tokenHash;
    @Column(name = "site_code", nullable = false, length = 64) private String siteCode;
    @Column(name = "expires_at", nullable = false) private Instant expiresAt;
    @Column(name = "consumed_at") private Instant consumedAt;
    @Column(name = "consumed_by_device", length = 64) private String consumedByDevice;

    public Long getId() { return id; }
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    public String getSiteCode() { return siteCode; }
    public void setSiteCode(String siteCode) { this.siteCode = siteCode; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public Instant getConsumedAt() { return consumedAt; }
    public void setConsumedAt(Instant consumedAt) { this.consumedAt = consumedAt; }
    public String getConsumedByDevice() { return consumedByDevice; }
    public void setConsumedByDevice(String consumedByDevice) { this.consumedByDevice = consumedByDevice; }
}