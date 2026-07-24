package com.hes.server.service;

import com.hes.server.domain.telemetry.TelemetryHistoryEntity;
import com.hes.server.domain.telemetry.TelemetryHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TelemetryQueryService {
    private final TelemetryHistoryRepository historyRepository;
    private final DeviceRegistryService deviceRegistryService;

    public TelemetryQueryService(TelemetryHistoryRepository historyRepository,
                                 DeviceRegistryService deviceRegistryService) {
        this.historyRepository = historyRepository;
        this.deviceRegistryService = deviceRegistryService;
    }

    public List<TelemetryHistoryEntity> history(String deviceId, Instant from, Instant to) {
        deviceRegistryService.requireDevice(deviceId);
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from must be before to");
        }
        return historyRepository.findRange(deviceId, from, to);
    }
}
