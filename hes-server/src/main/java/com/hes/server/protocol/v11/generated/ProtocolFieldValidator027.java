package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ProtocolFieldValidator027 implements ProtocolFieldValidator {
    @Override public String id() { return "PFV-027"; }
    @Override public String fieldName() { return "protocolVersion"; }
    @Override public Optional<String> validate(Object value) {
        return "1.1".equals(String.valueOf(value)) ? Optional.empty() : Optional.of("PFV-027 protocol must be 1.1");
    }
}