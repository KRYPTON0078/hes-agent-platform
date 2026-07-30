package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate046 implements DispatchPredicate {
    @Override public String id() { return "DSP-046"; }
    @Override public String description() { return "Demand response active flag 1 (DSP-046)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("dr_active", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("1")) >= 0;
    }
}