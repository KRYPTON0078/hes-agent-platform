package com.hes.server.energy.dispatch;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dispatch_policy")
public class DispatchPolicyEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "policy_code", nullable = false, unique = true, length = 64) private String policyCode;
    @Column(nullable = false, length = 128) private String name;
    @Column(nullable = false) private boolean enabled = true;
    @Column(nullable = false) private int priority = 100;
    @Column(name = "max_export_watts", precision = 12, scale = 2) private BigDecimal maxExportWatts;
    @Column(name = "soc_reserve_pct", precision = 5, scale = 2) private BigDecimal socReservePct;
    @Column(name = "demand_response", nullable = false) private boolean demandResponse;

    public Long getId() { return id; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public BigDecimal getMaxExportWatts() { return maxExportWatts; }
    public void setMaxExportWatts(BigDecimal maxExportWatts) { this.maxExportWatts = maxExportWatts; }
    public BigDecimal getSocReservePct() { return socReservePct; }
    public void setSocReservePct(BigDecimal socReservePct) { this.socReservePct = socReservePct; }
    public boolean isDemandResponse() { return demandResponse; }
    public void setDemandResponse(boolean demandResponse) { this.demandResponse = demandResponse; }
}