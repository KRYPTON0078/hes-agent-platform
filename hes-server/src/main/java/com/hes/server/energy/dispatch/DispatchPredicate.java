package com.hes.server.energy.dispatch;

import java.math.BigDecimal;
import java.util.Map;

public interface DispatchPredicate {
    String id();
    String description();
    boolean matches(Map<String, BigDecimal> signals);
}