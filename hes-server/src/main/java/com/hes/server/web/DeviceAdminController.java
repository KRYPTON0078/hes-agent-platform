package com.hes.server.web;

import com.hes.server.domain.device.DeviceEntity;
import com.hes.server.domain.device.DeviceStatus;
import com.hes.server.service.DeviceRegistryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/devices/{deviceId}/admin")
@Tag(name = "Device Admin")
public class DeviceAdminController {
    private final DeviceRegistryService deviceRegistryService;

    public DeviceAdminController(DeviceRegistryService deviceRegistryService) {
        this.deviceRegistryService = deviceRegistryService;
    }

    @PostMapping("/disable")
    public Map<String, Object> disable(@PathVariable String deviceId) {
        DeviceEntity device = deviceRegistryService.requireDevice(deviceId);
        device.setStatus(DeviceStatus.DISABLED);
        return Map.of("deviceId", deviceId, "status", device.getStatus());
    }

    @PostMapping("/enable")
    public Map<String, Object> enable(@PathVariable String deviceId) {
        DeviceEntity device = deviceRegistryService.requireDevice(deviceId);
        if (device.getStatus() == DeviceStatus.DISABLED) {
            device.setStatus(DeviceStatus.REGISTERED);
        }
        return Map.of("deviceId", deviceId, "status", device.getStatus());
    }
}
