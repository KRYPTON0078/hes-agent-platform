package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate078 implements DispatchPredicate {
    @Override public String id() { return "DSP-078"; }
    @Override public String description() { return "Site load above threshold 2.4 (DSP-078)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("load_kw", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("2.4")) > 0;
    }
}