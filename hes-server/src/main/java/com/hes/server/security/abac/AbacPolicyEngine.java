package com.hes.server.security.abac;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AbacPolicyEngine {
    private final List<AbacPolicy> policies;

    public AbacPolicyEngine(List<AbacPolicy> policies) {
        this.policies = List.copyOf(policies);
    }

    public boolean permits(AbacRequest request) {
        return policies.stream().allMatch(p -> p.permits(request));
    }
}
