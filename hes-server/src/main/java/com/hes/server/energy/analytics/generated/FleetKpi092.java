package com.hes.server.energy.analytics.generated;

import com.hes.server.energy.analytics.FleetKpi;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class FleetKpi092 implements FleetKpi {
    @Override public String id() { return "KPI-SOC-AVG-092"; }
    @Override public String title() { return "Average SOC variant 092"; }
    @Override public BigDecimal compute(Map<String, BigDecimal> inputs) {
        BigDecimal den = nz(inputs.get("device_count")); if (den.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO; return nz(inputs.get("soc_sum")).divide(den, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("0.97")).add(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
    }
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}