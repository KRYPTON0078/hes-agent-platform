package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate089 implements DispatchPredicate {
    @Override public String id() { return "DSP-089"; }
    @Override public String description() { return "PV production above threshold 2.75 (DSP-089)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("pv_kw", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("2.75")) > 0;
    }
}