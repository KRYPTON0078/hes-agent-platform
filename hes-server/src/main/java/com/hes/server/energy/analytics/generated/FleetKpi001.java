package com.hes.server.energy.analytics.generated;

import com.hes.server.energy.analytics.FleetKpi;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class FleetKpi001 implements FleetKpi {
    @Override public String id() { return "KPI-FAULT-001"; }
    @Override public String title() { return "Fault rate variant 001"; }
    @Override public BigDecimal compute(Map<String, BigDecimal> inputs) {
        BigDecimal den = nz(inputs.get("samples")); if (den.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO; return nz(inputs.get("faults")).divide(den, 6, RoundingMode.HALF_UP).multiply(new BigDecimal("0.86")).add(new BigDecimal("0.05")).setScale(6, RoundingMode.HALF_UP);
    }
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}