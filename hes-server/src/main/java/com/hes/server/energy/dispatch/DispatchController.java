package com.hes.server.energy.dispatch;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/dispatch")
public class DispatchController {
    private final DispatchEngine engine;
    private final DispatchPolicyRepository policyRepository;
    private final DispatchDecisionRepository decisionRepository;
    private final DispatchMqBridge mqBridge;

    public DispatchController(DispatchEngine engine,
                              DispatchPolicyRepository policyRepository,
                              DispatchDecisionRepository decisionRepository,
                              DispatchMqBridge mqBridge) {
        this.engine = engine;
        this.policyRepository = policyRepository;
        this.decisionRepository = decisionRepository;
        this.mqBridge = mqBridge;
    }

    @PostMapping("/policies")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public DispatchPolicyEntity create(@RequestBody DispatchPolicyEntity body) {
        return policyRepository.save(body);
    }

    @PostMapping("/evaluate/{deviceId}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public DispatchDecisionEntity evaluate(@PathVariable String deviceId, @RequestBody Map<String, BigDecimal> signals) {
        return engine.evaluate(deviceId, signals);
    }

    @GetMapping("/decisions/{deviceId}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN','VIEWER')")
    public List<DispatchDecisionEntity> decisions(@PathVariable String deviceId) {
        return decisionRepository.findTop50ByDeviceIdOrderByDecidedAtDesc(deviceId);
    }

    @PostMapping("/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Integer> publish() {
        return Map.of("published", mqBridge.publishPending());
    }
}