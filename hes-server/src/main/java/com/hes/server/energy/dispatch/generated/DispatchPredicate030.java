package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate030 implements DispatchPredicate {
    @Override public String id() { return "DSP-030"; }
    @Override public String description() { return "Export watts above threshold 2610 (DSP-030)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("export_watts", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("2610")) > 0;
    }
}