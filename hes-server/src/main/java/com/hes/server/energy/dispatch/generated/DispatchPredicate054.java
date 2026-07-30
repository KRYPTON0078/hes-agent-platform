package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate054 implements DispatchPredicate {
    @Override public String id() { return "DSP-054"; }
    @Override public String description() { return "Grid frequency below threshold 49.7 (DSP-054)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("grid_freq_hz", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("49.7")) < 0;
    }
}