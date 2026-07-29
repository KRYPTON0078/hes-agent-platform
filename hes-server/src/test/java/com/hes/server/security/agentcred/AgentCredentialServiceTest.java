package com.hes.server.security.agentcred;

import com.hes.server.domain.device.DeviceCredentialRepository;
import com.hes.server.domain.device.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AgentCredentialServiceTest {

    @Mock DeviceRepository deviceRepository;
    @Mock DeviceCredentialRepository credentialRepository;

    @Test
    void bcryptHashRoundTrip() {
        AgentCredentialService service = new AgentCredentialService(
                deviceRepository, credentialRepository, new BCryptPasswordEncoder(4), 8, 300);
        String hash = service.hashApiKey("super-secret-key");
        assertTrue(hash.startsWith("bcrypt:"));
        assertTrue(service.matches("super-secret-key", hash));
        assertFalse(service.matches("wrong", hash));
    }

    @Test
    void legacySha256StillMatchesDuringMigration() {
        AgentCredentialService service = new AgentCredentialService(
                deviceRepository, credentialRepository, new BCryptPasswordEncoder(4), 8, 300);
        String legacy = AgentCredentialService.sha256("legacy-key");
        assertTrue(service.matches("legacy-key", legacy));
    }
}
