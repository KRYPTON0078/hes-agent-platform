package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate002 implements DispatchPredicate {
    @Override public String id() { return "DSP-002"; }
    @Override public String description() { return "SOC above threshold 12 (DSP-002)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("soc", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("12")) > 0;
    }
}