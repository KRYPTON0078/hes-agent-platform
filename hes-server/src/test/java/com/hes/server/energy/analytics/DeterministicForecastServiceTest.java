package com.hes.server.energy.analytics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeterministicForecastServiceTest {
    @Mock EnergyForecastRepository repository;

    @Test
    void producesRequestedHorizon() {
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        DeterministicForecastService svc = new DeterministicForecastService(repository);
        List<EnergyForecastEntity> out = svc.forecastNextHours("D1", BigDecimal.valueOf(50), 6);
        assertEquals(6, out.size());
        assertEquals(DeterministicForecastService.MODEL, out.get(0).getModelVersion());
    }
}