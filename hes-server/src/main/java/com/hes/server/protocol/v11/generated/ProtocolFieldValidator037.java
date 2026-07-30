package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator037 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-037"; }
    @Override public String fieldName() { return "messageId"; }
    @Override public Optional<String> validate(Object value) {
        String s = value == null ? "" : String.valueOf(value); return s.length() >= 45 ? Optional.empty() : Optional.of("PFV-037 messageId short");
    }
}