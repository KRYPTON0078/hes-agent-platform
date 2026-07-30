package com.hes.server.energy.analytics.generated;

import com.hes.server.energy.analytics.FleetKpi;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class FleetKpi005 implements FleetKpi {
    @Override public String id() { return "KPI-IMPORT-005"; }
    @Override public String title() { return "Import share variant 005"; }
    @Override public BigDecimal compute(Map<String, BigDecimal> inputs) {
        BigDecimal den = nz(inputs.get("load_kwh")); if (den.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO; return nz(inputs.get("import_kwh")).divide(den, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("0.9")).add(new BigDecimal("0.25")).setScale(4, RoundingMode.HALF_UP);
    }
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}