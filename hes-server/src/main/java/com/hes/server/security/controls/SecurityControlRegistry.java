package com.hes.server.security.controls;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SecurityControlRegistry {
    private final List<SecurityControl> controls;

    public SecurityControlRegistry(List<SecurityControl> controls) {
        this.controls = List.copyOf(controls);
    }

    public List<SecurityControl> all() {
        return controls;
    }

    public List<SecurityControl> failing(SecurityControlContext context) {
        return controls.stream().filter(c -> !c.evaluate(context)).toList();
    }
}
