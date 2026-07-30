package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate019 implements DispatchPredicate {
    @Override public String id() { return "DSP-019"; }
    @Override public String description() { return "PV production above threshold 5.25 (DSP-019)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("pv_kw", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("5.25")) > 0;
    }
}