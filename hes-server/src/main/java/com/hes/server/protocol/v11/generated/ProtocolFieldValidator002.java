package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator002 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-002"; }
    @Override public String fieldName() { return "capabilityFlags"; }
    @Override public Optional<String> validate(Object value) {
        if (value == null) return Optional.of("PFV-002 missing caps"); long v; try { v = Long.parseLong(String.valueOf(value)); } catch (Exception e) { return Optional.of("PFV-002 caps not long"); } return v >= 0 ? Optional.empty() : Optional.of("PFV-002 caps negative");
    }
}