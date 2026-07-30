package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate068 implements DispatchPredicate {
    @Override public String id() { return "DSP-068"; }
    @Override public String description() { return "Site load above threshold 3.9 (DSP-068)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("load_kw", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("3.9")) > 0;
    }
}