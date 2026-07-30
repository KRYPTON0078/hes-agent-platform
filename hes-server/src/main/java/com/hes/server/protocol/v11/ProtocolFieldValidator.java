package com.hes.server.protocol.v11;

import java.util.Optional;

public interface ProtocolFieldValidator {
    String id();
    String fieldName();
    Optional<String> validate(Object value);
}