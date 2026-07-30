package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator061 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-061"; }
    @Override public String fieldName() { return "messageId"; }
    @Override public Optional<String> validate(Object value) {
        String s = value == null ? "" : String.valueOf(value); return s.length() >= 29 ? Optional.empty() : Optional.of("PFV-061 messageId short");
    }
}