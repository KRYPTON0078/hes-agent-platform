package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate060 implements DispatchPredicate {
    @Override public String id() { return "DSP-060"; }
    @Override public String description() { return "Export watts above threshold 3720 (DSP-060)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("export_watts", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("3720")) > 0;
    }
}