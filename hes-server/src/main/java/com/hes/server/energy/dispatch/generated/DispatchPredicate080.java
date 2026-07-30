package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate080 implements DispatchPredicate {
    @Override public String id() { return "DSP-080"; }
    @Override public String description() { return "Export watts above threshold 4460 (DSP-080)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("export_watts", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("4460")) > 0;
    }
}