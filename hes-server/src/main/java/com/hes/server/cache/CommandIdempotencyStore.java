package com.hes.server.cache;

/**
 * Cross-instance idempotency guard for command issuance.
 */
public interface CommandIdempotencyStore {
    /**
     * @return true if this key was claimed (first writer), false if already claimed
     */
    boolean tryClaim(String idempotencyKey, String commandId);
}
