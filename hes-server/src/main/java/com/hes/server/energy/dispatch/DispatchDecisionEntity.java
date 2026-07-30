package com.hes.server.energy.dispatch;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dispatch_decision")
public class DispatchDecisionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "policy_id", nullable = false) private Long policyId;
    @Column(name = "device_id", nullable = false, length = 64) private String deviceId;
    @Column(name = "decided_action", nullable = false, length = 32) private String decidedAction;
    @Column(nullable = false, length = 255) private String reason;
    @Column(name = "signal_snapshot_json", columnDefinition = "TEXT") private String signalSnapshotJson;
    @Column(name = "decided_at", nullable = false) private Instant decidedAt = Instant.now();

    public Long getId() { return id; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getDecidedAction() { return decidedAction; }
    public void setDecidedAction(String decidedAction) { this.decidedAction = decidedAction; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getSignalSnapshotJson() { return signalSnapshotJson; }
    public void setSignalSnapshotJson(String signalSnapshotJson) { this.signalSnapshotJson = signalSnapshotJson; }
    public Instant getDecidedAt() { return decidedAt; }
}