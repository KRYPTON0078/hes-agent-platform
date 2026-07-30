package com.hes.agent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/** Builds protocol v1.1 envelopes with capability flags, seq, and nonce. */
public final class ProtocolV11Envelope {
    private final AtomicLong seq = new AtomicLong();
    private final long capabilityFlags;

    public ProtocolV11Envelope(long capabilityFlags) {
        this.capabilityFlags = capabilityFlags;
    }

    public Map<String, Object> wrap(Map<String, Object> payload) {
        Map<String, Object> env = new LinkedHashMap<>(payload);
        env.put("protocolVersion", "1.1");
        env.put("capabilityFlags", capabilityFlags);
        env.put("seq", seq.incrementAndGet());
        env.put("nonce", UUID.randomUUID().toString().replace("-", ""));
        return env;
    }
}