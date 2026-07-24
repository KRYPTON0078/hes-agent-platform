package com.hes.server.health;

import com.hes.server.domain.device.DeviceRepository;
import com.hes.server.presence.OnlinePresenceStore;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AgentFleetHealthIndicator implements HealthIndicator {
    private final DeviceRepository deviceRepository;
    private final OnlinePresenceStore presenceStore;

    public AgentFleetHealthIndicator(DeviceRepository deviceRepository, OnlinePresenceStore presenceStore) {
        this.deviceRepository = deviceRepository;
        this.presenceStore = presenceStore;
    }

    @Override
    public Health health() {
        long total = deviceRepository.count();
        long online = presenceStore.onlineCount();
        Health.Builder builder = total == 0 || online > 0 ? Health.up() : Health.down();
        return builder
                .withDetail("devices", total)
                .withDetail("online", online)
                .build();
    }
}
