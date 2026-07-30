package com.hes.server.protocol.v11;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ProtocolV11ValidatorPropertyTest {
    @Test
    void collectsErrorsFromAllValidators() {
        ProtocolFieldValidator a = new ProtocolFieldValidator() {
            public String id() { return "a"; }
            public String fieldName() { return "seq"; }
            public Optional<String> validate(Object v) { return v == null ? Optional.of("missing") : Optional.empty(); }
        };
        ProtocolFieldValidator b = new ProtocolFieldValidator() {
            public String id() { return "b"; }
            public String fieldName() { return "nonce"; }
            public Optional<String> validate(Object v) { return Optional.of("bad"); }
        };
        ProtocolV11Validator validator = new ProtocolV11Validator(List.of(a, b));
        List<String> errors = validator.validate(Map.of("seq", 1L));
        assertEquals(1, errors.size());
        assertEquals("bad", errors.get(0));
    }
}