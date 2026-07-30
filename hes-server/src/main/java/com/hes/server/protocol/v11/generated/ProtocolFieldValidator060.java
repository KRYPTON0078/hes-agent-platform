package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator060 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-060"; }
    @Override public String fieldName() { return "deviceId"; }
    @Override public Optional<String> validate(Object value) {
        if (value == null || String.valueOf(value).isBlank()) return Optional.of("PFV-060 blank"); return String.valueOf(value).length() <= 28 + 32 ? Optional.empty() : Optional.of("PFV-060 too long");
    }
}