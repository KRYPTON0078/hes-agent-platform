package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator028 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-028"; }
    @Override public String fieldName() { return "deviceId"; }
    @Override public Optional<String> validate(Object value) {
        if (value == null || String.valueOf(value).isBlank()) return Optional.of("PFV-028 blank"); return String.valueOf(value).length() <= 36 + 32 ? Optional.empty() : Optional.of("PFV-028 too long");
    }
}