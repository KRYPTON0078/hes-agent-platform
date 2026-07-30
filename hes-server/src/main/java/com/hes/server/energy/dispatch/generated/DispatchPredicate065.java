package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate065 implements DispatchPredicate {
    @Override public String id() { return "DSP-065"; }
    @Override public String description() { return "Grid frequency above threshold 50.5 (DSP-065)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("grid_freq_hz", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("50.5")) > 0;
    }
}