package com.hes.server.energy.dispatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispatchEngineSocReserveTest {
    @Mock DispatchPolicyRepository policyRepository;
    @Mock DispatchDecisionRepository decisionRepository;
    @Mock DispatchEventRepository eventRepository;

    @Test
    void reservesChargeWhenSocBelowPolicy() {
        DispatchPolicyEntity policy = new DispatchPolicyEntity();
        policy.setPolicyCode("RES-1");
        policy.setName("Reserve");
        policy.setPriority(10);
        policy.setEnabled(true);
        policy.setSocReservePct(BigDecimal.valueOf(30));
        when(policyRepository.findByEnabledTrueOrderByPriorityAsc()).thenReturn(List.of(policy));
        when(decisionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(eventRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DispatchAuditHook audit = org.mockito.Mockito.mock(DispatchAuditHook.class);
        DispatchEngine engine = new DispatchEngine(policyRepository, decisionRepository, eventRepository, List.of(), new ObjectMapper(), audit);
        // policy id null until persisted; engine uses getId which is null -> 0L path still records action
        DispatchDecisionEntity d = engine.evaluate("D1", Map.of("soc", BigDecimal.valueOf(20)));
        assertEquals("RESERVE_CHARGE", d.getDecidedAction());
    }
}