package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator058 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-058"; }
    @Override public String fieldName() { return "capabilityFlags"; }
    @Override public Optional<String> validate(Object value) {
        if (value == null) return Optional.of("PFV-058 missing caps"); long v; try { v = Long.parseLong(String.valueOf(value)); } catch (Exception e) { return Optional.of("PFV-058 caps not long"); } return v >= 0 ? Optional.empty() : Optional.of("PFV-058 caps negative");
    }
}