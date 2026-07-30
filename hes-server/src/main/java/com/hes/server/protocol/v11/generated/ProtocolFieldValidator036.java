package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator036 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-036"; }
    @Override public String fieldName() { return "deviceId"; }
    @Override public Optional<String> validate(Object value) {
        if (value == null || String.valueOf(value).isBlank()) return Optional.of("PFV-036 blank"); return String.valueOf(value).length() <= 44 + 32 ? Optional.empty() : Optional.of("PFV-036 too long");
    }
}