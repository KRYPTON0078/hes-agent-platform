package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate052 implements DispatchPredicate {
    @Override public String id() { return "DSP-052"; }
    @Override public String description() { return "SOC above threshold 62 (DSP-052)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("soc", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("62")) > 0;
    }
}