package com.hes.server.protocol.v11;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProtocolV11Validator {
    private final List<ProtocolFieldValidator> validators;

    public ProtocolV11Validator(List<ProtocolFieldValidator> validators) {
        this.validators = validators;
    }

    public List<String> validate(Map<String, Object> envelope) {
        List<String> errors = new ArrayList<>();
        for (ProtocolFieldValidator v : validators) {
            Object value = envelope.get(v.fieldName());
            v.validate(value).ifPresent(errors::add);
        }
        return errors;
    }
}