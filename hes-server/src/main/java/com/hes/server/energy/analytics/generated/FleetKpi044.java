package com.hes.server.energy.analytics.generated;

import com.hes.server.energy.analytics.FleetKpi;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class FleetKpi044 implements FleetKpi {
    @Override public String id() { return "KPI-EXPORT-044"; }
    @Override public String title() { return "Export utilization variant 044"; }
    @Override public BigDecimal compute(Map<String, BigDecimal> inputs) {
        BigDecimal den = nz(inputs.get("capacity_kwh")); if (den.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO; return nz(inputs.get("export_kwh")).divide(den, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("0.89")).add(new BigDecimal("0.1")).setScale(4, RoundingMode.HALF_UP);
    }
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}