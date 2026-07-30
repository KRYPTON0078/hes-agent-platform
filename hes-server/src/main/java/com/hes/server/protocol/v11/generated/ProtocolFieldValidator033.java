package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator033 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-033"; }
    @Override public String fieldName() { return "nonce"; }
    @Override public Optional<String> validate(Object value) {
        if (value == null) return Optional.of("PFV-033 missing nonce"); String s = String.valueOf(value); return s.matches("[0-9a-fA-F]{41,64}") ? Optional.empty() : Optional.of("PFV-033 bad nonce");
    }
}