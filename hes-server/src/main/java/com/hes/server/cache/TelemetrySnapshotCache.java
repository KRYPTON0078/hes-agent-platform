package com.hes.server.cache;

import com.hes.common.protocol.TelemetryPayload;

import java.util.Optional;

/**
 * Hot-path latest telemetry snapshot (Redis when available, otherwise no-op).
 */
public interface TelemetrySnapshotCache {
    void put(String deviceId, TelemetryPayload payload);

    Optional<TelemetryPayload> get(String deviceId);
}
