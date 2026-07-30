package com.hes.server.ota;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtaJobServiceTransitionTest {
    @Mock OtaJobRepository repository;

    @Test
    void transitionsCreatedToDownloading() {
        OtaPhaseHandler handler = new OtaPhaseHandler() {
            public String id() { return "t"; }
            public String fromPhase() { return "CREATED"; }
            public String toPhase() { return "DOWNLOADING"; }
            public boolean canTransition(String c, boolean d, boolean a, boolean k) {
                return "CREATED".equals(c) && d && !a && !k;
            }
        };
        OtaJobEntity job = new OtaJobEntity();
        job.setJobCode("OTA-1");
        job.setPhase("CREATED");
        when(repository.findByJobCode("OTA-1")).thenReturn(Optional.of(job));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        OtaJobService svc = new OtaJobService(repository, List.of(handler));
        OtaJobEntity out = svc.transition("OTA-1", true, false, false);
        assertEquals("DOWNLOADING", out.getPhase());
    }
}