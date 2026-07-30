package com.hes.server.energy.analytics;

import java.math.BigDecimal;
import java.util.Map;

public interface FleetKpi {
    String id();
    String title();
    BigDecimal compute(Map<String, BigDecimal> inputs);
}