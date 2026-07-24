package com.hes.server.service;

import com.hes.common.error.ErrorCode;
import com.hes.common.protocol.AgentMessage;
import com.hes.common.protocol.MessageType;
import com.hes.server.domain.device.*;
import com.hes.server.domain.site.SiteEntity;
import com.hes.server.domain.site.SiteRepository;
import com.hes.server.presence.OnlinePresenceStore;
import com.hes.server.web.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;

@Service
public class DeviceRegistryService {

    private final DeviceRepository deviceRepository;
    private final DeviceCredentialRepository credentialRepository;
    private final SiteRepository siteRepository;
    private final OnlinePresenceStore presenceStore;

    public DeviceRegistryService(DeviceRepository deviceRepository,
                                 DeviceCredentialRepository credentialRepository,
                                 SiteRepository siteRepository,
                                 OnlinePresenceStore presenceStore) {
        this.deviceRepository = deviceRepository;
        this.credentialRepository = credentialRepository;
        this.siteRepository = siteRepository;
        this.presenceStore = presenceStore;
    }

    @Transactional
    public AgentMessage register(AgentMessage message) {
        requireType(message, MessageType.AGENT_REGISTER);
        Map<String, Object> payload = message.payload() == null ? Map.of() : message.payload();
        String model = stringVal(payload.get("model"), "HES-GENERIC");
        String firmware = stringVal(payload.get("firmwareVersion"), "0.0.0");
        String apiKey = stringVal(payload.get("apiKey"), UUID.randomUUID().toString().replace("-", ""));
        String siteCode = stringVal(payload.get("siteCode"), "SITE-DEFAULT");
        String siteName = stringVal(payload.get("siteName"), "Default Household");

        SiteEntity site = siteRepository.findBySiteCode(siteCode).orElseGet(() -> {
            SiteEntity created = new SiteEntity();
            created.setSiteCode(siteCode);
            created.setName(siteName);
            created.setTimezone(stringVal(payload.get("timezone"), "UTC"));
            return siteRepository.save(created);
        });

        DeviceEntity device = deviceRepository.findByDeviceId(message.deviceId())
                .orElseGet(DeviceEntity::new);
        device.setDeviceId(message.deviceId());
        device.setSite(site);
        device.setModel(model);
        device.setFirmwareVersion(firmware);
        device.setStatus(DeviceStatus.ONLINE);
        device.setLastSeenAt(Instant.now());
        device = deviceRepository.save(device);

        DeviceCredentialEntity credential = credentialRepository.findByDevice_Id(device.getId())
                .orElseGet(DeviceCredentialEntity::new);
        credential.setDevice(device);
        credential.setApiKeyHash(sha256(apiKey));
        credential.setActive(true);
        credentialRepository.save(credential);

        presenceStore.heartbeat(device.getDeviceId(), Instant.now());

        return AgentMessage.of(
                MessageType.AGENT_REGISTER_ACK,
                UUID.randomUUID().toString(),
                device.getDeviceId(),
                Map.of(
                        "status", "OK",
                        "apiKey", apiKey,
                        "siteCode", site.getSiteCode(),
                        "heartbeatIntervalSeconds", 30
                )
        );
    }

    @Transactional
    public AgentMessage heartbeat(AgentMessage message) {
        requireType(message, MessageType.HEARTBEAT);
        DeviceEntity device = requireDevice(message.deviceId());
        device.setLastSeenAt(Instant.now());
        if (device.getStatus() != DeviceStatus.DISABLED) {
            device.setStatus(DeviceStatus.ONLINE);
        }
        deviceRepository.save(device);
        presenceStore.heartbeat(device.getDeviceId(), Instant.now());
        return AgentMessage.of(
                MessageType.HEARTBEAT_ACK,
                UUID.randomUUID().toString(),
                device.getDeviceId(),
                Map.of("status", "OK", "serverTime", Instant.now().toString())
        );
    }

    public void assertApiKey(String deviceId, String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Missing X-Api-Key");
        }
        DeviceEntity device = requireDevice(deviceId);
        DeviceCredentialEntity credential = credentialRepository.findByDevice_Id(device.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "No credential for device"));
        if (!credential.isActive() || !credential.getApiKeyHash().equals(sha256(apiKey))) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid API key");
        }
    }

    public DeviceEntity requireDevice(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEVICE_NOT_FOUND, "Device not found: " + deviceId));
    }

    private static void requireType(AgentMessage message, MessageType expected) {
        if (message.type() != expected) {
            throw new BusinessException(ErrorCode.PROTOCOL_UNSUPPORTED, "Expected " + expected);
        }
        if (message.deviceId() == null || message.deviceId().isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "deviceId is required");
        }
    }

    private static String stringVal(Object value, String fallback) {
        return value == null ? fallback : String.valueOf(value);
    }

    static String sha256(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(raw.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
