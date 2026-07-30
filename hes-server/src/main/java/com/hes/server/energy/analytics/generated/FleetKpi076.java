package com.hes.server.energy.analytics.generated;

import com.hes.server.energy.analytics.FleetKpi;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class FleetKpi076 implements FleetKpi {
    @Override public String id() { return "KPI-PEAK-SHV-076"; }
    @Override public String title() { return "Peak shave ratio variant 076"; }
    @Override public BigDecimal compute(Map<String, BigDecimal> inputs) {
        BigDecimal den = nz(inputs.get("peak_kw")); if (den.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO; return nz(inputs.get("peak_avoided_kw")).divide(den, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("1.01")).add(new BigDecimal("0.3")).setScale(4, RoundingMode.HALF_UP);
    }
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}