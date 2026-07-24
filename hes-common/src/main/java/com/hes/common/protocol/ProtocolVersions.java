package com.hes.common.protocol;

import java.util.Set;

public final class ProtocolVersions {
    public static final String V1 = "1.0";
    public static final Set<String> SUPPORTED = Set.of(V1);

    private ProtocolVersions() {}

    public static boolean isSupported(String version) {
        return version != null && SUPPORTED.contains(version);
    }
}
