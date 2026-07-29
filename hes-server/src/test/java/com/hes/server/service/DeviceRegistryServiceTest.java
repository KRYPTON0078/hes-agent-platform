package com.hes.server.service;

import com.hes.common.protocol.AgentMessage;
import com.hes.common.protocol.MessageType;
import com.hes.server.config.HesProperties;
import com.hes.server.domain.device.DeviceCredentialRepository;
import com.hes.server.domain.device.DeviceRepository;
import com.hes.server.domain.site.SiteRepository;
import com.hes.server.presence.InMemoryOnlinePresenceStore;
import com.hes.server.security.agentcred.AgentCredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceRegistryServiceTest {

    @Mock DeviceRepository deviceRepository;
    @Mock DeviceCredentialRepository credentialRepository;
    @Mock SiteRepository siteRepository;

    DeviceRegistryService service;

    @BeforeEach
    void setUp() {
        HesProperties properties = new HesProperties();
        AgentCredentialService agentCredentialService = new AgentCredentialService(
                deviceRepository, credentialRepository, new BCryptPasswordEncoder(4), 8, 300);
        service = new DeviceRegistryService(
                deviceRepository,
                credentialRepository,
                siteRepository,
                new InMemoryOnlinePresenceStore(properties),
                agentCredentialService
        );
        when(deviceRepository.findByDeviceId(any())).thenReturn(Optional.empty());
        when(deviceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(credentialRepository.findByDevice_Id(any())).thenReturn(Optional.empty());
        when(credentialRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(siteRepository.findBySiteCode(any())).thenReturn(Optional.empty());
        when(siteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void registerReturnsAckWithApiKeyAndSite() {
        AgentMessage request = AgentMessage.of(
                MessageType.AGENT_REGISTER,
                UUID.randomUUID().toString(),
                "HES-TEST-1",
                Map.of("model", "TEST", "siteCode", "SITE-A")
        );
        AgentMessage ack = service.register(request);
        assertEquals(MessageType.AGENT_REGISTER_ACK, ack.type());
        assertEquals("HES-TEST-1", ack.deviceId());
        assertNotNull(ack.payload().get("apiKey"));
        assertEquals("SITE-A", ack.payload().get("siteCode"));
    }
}
