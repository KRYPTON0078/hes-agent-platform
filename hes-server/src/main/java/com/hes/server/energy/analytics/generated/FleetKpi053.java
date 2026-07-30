package com.hes.server.energy.analytics.generated;

import com.hes.server.energy.analytics.FleetKpi;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class FleetKpi053 implements FleetKpi {
    @Override public String id() { return "KPI-THPUT-053"; }
    @Override public String title() { return "Energy throughput kWh variant 053"; }
    @Override public BigDecimal compute(Map<String, BigDecimal> inputs) {
        return nz(inputs.get("energy_in")).add(nz(inputs.get("energy_out"))).multiply(new BigDecimal("0.98")).add(new BigDecimal("0.2")).setScale(3, RoundingMode.HALF_UP);
    }
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}