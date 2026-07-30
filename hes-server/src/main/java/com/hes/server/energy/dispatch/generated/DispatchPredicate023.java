package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate023 implements DispatchPredicate {
    @Override public String id() { return "DSP-023"; }
    @Override public String description() { return "Import watts above threshold 1467 (DSP-023)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("import_watts", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("1467")) > 0;
    }
}