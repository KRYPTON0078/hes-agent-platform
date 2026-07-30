package com.hes.server.energy.analytics.generated;

import com.hes.server.energy.analytics.FleetKpi;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class FleetKpi029 implements FleetKpi {
    @Override public String id() { return "KPI-CMD-029"; }
    @Override public String title() { return "Command ACK rate variant 029"; }
    @Override public BigDecimal compute(Map<String, BigDecimal> inputs) {
        BigDecimal den = nz(inputs.get("cmd_sent")); if (den.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO; return nz(inputs.get("cmd_ack")).divide(den, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("0.94")).add(new BigDecimal("0.05")).setScale(4, RoundingMode.HALF_UP);
    }
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}