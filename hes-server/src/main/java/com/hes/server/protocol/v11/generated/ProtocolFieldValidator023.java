package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator023 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-023"; }
    @Override public String fieldName() { return "siteCode"; }
    @Override public Optional<String> validate(Object value) {
        String s = value == null ? "" : String.valueOf(value); return s.matches("SITE-[A-Z0-9]{2,31}") ? Optional.empty() : Optional.of("PFV-023 bad site");
    }
}