package com.hes.server.security.incident;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class DeviceQuarantineService {

    private final Map<String, String> quarantined = new java.util.concurrent.ConcurrentHashMap<>();

    public void quarantine(String deviceId, String reason) {
        quarantined.put(deviceId, reason == null ? "unspecified" : reason);
    }

    public boolean isQuarantined(String deviceId) {
        return quarantined.containsKey(deviceId);
    }

    public void release(String deviceId) {
        quarantined.remove(deviceId);
    }

    public List<String> allQuarantined() {
        return List.copyOf(quarantined.keySet());
    }
}
