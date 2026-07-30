package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate033 implements DispatchPredicate {
    @Override public String id() { return "DSP-033"; }
    @Override public String description() { return "Import watts above threshold 1757 (DSP-033)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("import_watts", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("1757")) > 0;
    }
}