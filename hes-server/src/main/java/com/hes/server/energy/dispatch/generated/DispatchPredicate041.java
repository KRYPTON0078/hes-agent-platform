package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class DispatchPredicate041 implements DispatchPredicate {
    @Override public String id() { return "DSP-041"; }
    @Override public String description() { return "SOC below threshold 51 (DSP-041)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("soc", BigDecimal.ZERO);
        return v.compareTo(new BigDecimal("51")) < 0;
    }
}