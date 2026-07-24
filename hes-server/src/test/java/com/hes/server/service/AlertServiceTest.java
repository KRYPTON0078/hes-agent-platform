package com.hes.server.service;

import com.hes.common.protocol.TelemetryPayload;
import com.hes.server.config.HesProperties;
import com.hes.server.domain.alert.AlertEntity;
import com.hes.server.domain.alert.AlertRepository;
import com.hes.server.domain.alert.AlertStatus;
import com.hes.server.domain.device.DeviceEntity;
import com.hes.server.domain.device.DeviceRepository;
import com.hes.server.domain.device.DeviceStatus;
import com.hes.server.presence.InMemoryOnlinePresenceStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock AlertRepository alertRepository;
    @Mock DeviceRepository deviceRepository;

    AlertService service;
    DeviceEntity device;

    @BeforeEach
    void setUp() {
        HesProperties properties = new HesProperties();
        properties.getAlert().setLowSocThreshold(15.0);
        service = new AlertService(
                alertRepository,
                deviceRepository,
                new InMemoryOnlinePresenceStore(properties),
                properties
        );
        device = new DeviceEntity();
        device.setDeviceId("HES-1");
        // simulate persisted id via reflection-free setter absence: use spy pattern through mock find
        lenient().when(alertRepository.findFirstByDevice_IdAndAlertTypeAndStatus(any(), any(), any()))
                .thenReturn(Optional.empty());
        lenient().when(alertRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void evaluateTelemetryOpensLowSocAlert() {
        TelemetryPayload payload = new TelemetryPayload(
                BigDecimal.valueOf(10), null, null, null, null, null, null, 0, null, "IDLE"
        );
        // device id used by openIfAbsent — repository looks up by device.getId() which is null; still works
        service.evaluateTelemetry(device, payload);
        verify(alertRepository).save(any(AlertEntity.class));
    }

    @Test
    void scanOfflineDoesNotDoubleCountExistingAlert() {
        device.setStatus(DeviceStatus.OFFLINE);
        when(deviceRepository.findAll()).thenReturn(List.of(device));
        AlertEntity existing = new AlertEntity();
        existing.setStatus(AlertStatus.OPEN);
        when(alertRepository.findFirstByDevice_IdAndAlertTypeAndStatus(any(), eq(AlertService.TYPE_OFFLINE), eq(AlertStatus.OPEN)))
                .thenReturn(Optional.of(existing));

        int opened = service.scanOfflineDevices();
        assertEquals(0, opened);
        verify(alertRepository, never()).save(any());
    }
}
