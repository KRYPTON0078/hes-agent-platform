package com.hes.server.energy.analytics;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FleetKpiService {
    private final List<FleetKpi> kpis;

    public FleetKpiService(List<FleetKpi> kpis) {
        this.kpis = kpis;
    }

    public Map<String, BigDecimal> evaluateAll(Map<String, BigDecimal> inputs) {
        Map<String, BigDecimal> out = new LinkedHashMap<>();
        for (FleetKpi kpi : kpis) {
            out.put(kpi.id(), kpi.compute(inputs));
        }
        return out;
    }

    public List<FleetKpi> catalog() {
        return List.copyOf(kpis);
    }
}