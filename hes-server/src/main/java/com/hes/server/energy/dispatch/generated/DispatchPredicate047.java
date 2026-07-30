package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate047 implements DispatchPredicate {
    @Override public String id() { return "DSP-047"; }
    @Override public String description() { return "Battery temperature above threshold 42 (DSP-047)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("temp_c", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("42")) > 0;
    }
}