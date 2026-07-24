package com.hes.server.presence;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

/**
 * Tracks Agent online presence (Redis in docker profile, in-memory for local).
 */
public interface OnlinePresenceStore {
    void heartbeat(String deviceId, Instant seenAt);
    boolean isOnline(String deviceId);
    Optional<Instant> lastSeen(String deviceId);
    Set<String> onlineDeviceIds();
    long onlineCount();
}
