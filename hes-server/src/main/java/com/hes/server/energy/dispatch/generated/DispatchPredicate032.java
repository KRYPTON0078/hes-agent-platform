package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate032 implements DispatchPredicate {
    @Override public String id() { return "DSP-032"; }
    @Override public String description() { return "SOC above threshold 42 (DSP-032)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("soc", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("42")) > 0;
    }
}