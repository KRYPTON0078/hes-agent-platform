package com.hes.server.energy.dispatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DispatchEngine {
    private final DispatchPolicyRepository policyRepository;
    private final DispatchDecisionRepository decisionRepository;
    private final DispatchEventRepository eventRepository;
    private final List<DispatchPredicate> predicates;
    private final ObjectMapper objectMapper;
    private final DispatchAuditHook auditHook;

    public DispatchEngine(DispatchPolicyRepository policyRepository,
                          DispatchDecisionRepository decisionRepository,
                          DispatchEventRepository eventRepository,
                          List<DispatchPredicate> predicates,
                          ObjectMapper objectMapper,
                          DispatchAuditHook auditHook) {
        this.policyRepository = policyRepository;
        this.decisionRepository = decisionRepository;
        this.eventRepository = eventRepository;
        this.predicates = predicates;
        this.objectMapper = objectMapper;
        this.auditHook = auditHook;
    }

    @Transactional
    public DispatchDecisionEntity evaluate(String deviceId, Map<String, BigDecimal> signals) {
        List<DispatchPolicyEntity> policies = policyRepository.findByEnabledTrueOrderByPriorityAsc();
        String action = "HOLD";
        String reason = "No policy matched";
        Long policyId = 0L;
        for (DispatchPolicyEntity policy : policies) {
            if (policy.getSocReservePct() != null) {
                BigDecimal soc = signals.getOrDefault("soc", BigDecimal.ZERO);
                if (soc.compareTo(policy.getSocReservePct()) < 0) {
                    action = "RESERVE_CHARGE";
                    reason = "SOC below reserve " + policy.getPolicyCode();
                    policyId = policy.getId() == null ? 0L : policy.getId();
                    break;
                }
            }
            if (policy.getMaxExportWatts() != null) {
                BigDecimal exportW = signals.getOrDefault("export_watts", BigDecimal.ZERO).abs();
                if (exportW.compareTo(policy.getMaxExportWatts()) > 0) {
                    action = "LIMIT_EXPORT";
                    reason = "Export exceeds limit " + policy.getPolicyCode();
                    policyId = policy.getId() == null ? 0L : policy.getId();
                    break;
                }
            }
            if (policy.isDemandResponse() && signals.getOrDefault("dr_active", BigDecimal.ZERO).compareTo(BigDecimal.ONE) >= 0) {
                action = "DR_DISCHARGE";
                reason = "Demand response " + policy.getPolicyCode();
                policyId = policy.getId() == null ? 0L : policy.getId();
                break;
            }
        }
        for (DispatchPredicate predicate : predicates) {
            if (predicate.matches(signals)) {
                action = "PREDICATE_" + predicate.id();
                reason = predicate.description();
                break;
            }
        }
        DispatchDecisionEntity decision = new DispatchDecisionEntity();
        decision.setPolicyId(policyId);
        decision.setDeviceId(deviceId);
        decision.setDecidedAction(action);
        decision.setReason(reason);
        try {
            decision.setSignalSnapshotJson(objectMapper.writeValueAsString(signals));
        } catch (Exception e) {
            decision.setSignalSnapshotJson("{}");
        }
        decisionRepository.save(decision);
        auditHook.onDecision(decision);

        DispatchEventEntity event = new DispatchEventEntity();
        event.setEventId(UUID.randomUUID().toString());
        event.setDeviceId(deviceId);
        event.setIntent(action);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("action", action);
        payload.put("reason", reason);
        try {
            event.setPayloadJson(objectMapper.writeValueAsString(payload));
        } catch (Exception e) {
            event.setPayloadJson("{\"action\":\"" + action + "\"}");
        }
        event.setPublished(false);
        eventRepository.save(event);
        return decision;
    }
}