package com.hes.server.domain.device;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class DeviceStatusTransitions {
    private static final Map<DeviceStatus, Set<DeviceStatus>> ALLOWED = Map.of(
            DeviceStatus.REGISTERED, EnumSet.of(DeviceStatus.ONLINE, DeviceStatus.DISABLED),
            DeviceStatus.ONLINE, EnumSet.of(DeviceStatus.OFFLINE, DeviceStatus.DISABLED),
            DeviceStatus.OFFLINE, EnumSet.of(DeviceStatus.ONLINE, DeviceStatus.DISABLED),
            DeviceStatus.DISABLED, EnumSet.of(DeviceStatus.REGISTERED)
    );

    private DeviceStatusTransitions() {}

    public static boolean canTransition(DeviceStatus from, DeviceStatus to) {
        return ALLOWED.getOrDefault(from, Set.of()).contains(to);
    }
}
