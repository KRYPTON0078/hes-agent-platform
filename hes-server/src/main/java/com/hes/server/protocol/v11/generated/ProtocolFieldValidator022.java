package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator022 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-022"; }
    @Override public String fieldName() { return "firmwareVersion"; }
    @Override public Optional<String> validate(Object value) {
        String s = value == null ? "" : String.valueOf(value); return s.matches("\\d+\\.\\d+\\.\\d+.*") ? Optional.empty() : Optional.of("PFV-022 bad semver");
    }
}