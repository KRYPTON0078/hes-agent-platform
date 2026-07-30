package com.hes.server.security.enrollment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentTokenServiceTest {
    @Mock EnrollmentTokenRepository repository;

    @Test
    void issueAndConsumeHappyPath() {
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        EnrollmentTokenService svc = new EnrollmentTokenService(repository);
        String raw = svc.issue("SITE-DEMO", Instant.now().plusSeconds(3600));
        ArgumentCaptor<EnrollmentTokenEntity> cap = ArgumentCaptor.forClass(EnrollmentTokenEntity.class);
        verify(repository).save(cap.capture());
        when(repository.findByTokenHash(cap.getValue().getTokenHash())).thenReturn(Optional.of(cap.getValue()));
        String site = svc.consume(raw, "HES-1");
        assertEquals("SITE-DEMO", site);
        assertNotNull(cap.getValue().getConsumedAt());
    }
}