package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator048 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-048"; }
    @Override public String fieldName() { return "seq"; }
    @Override public Optional<String> validate(Object value) {
        if (value == null) return Optional.of("PFV-048 missing seq"); long v; try { v = Long.parseLong(String.valueOf(value)); } catch (Exception e) { return Optional.of("PFV-048 seq not long"); } return v > 3 ? Optional.empty() : Optional.of("PFV-048 seq too small");
    }
}