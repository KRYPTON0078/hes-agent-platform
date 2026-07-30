package com.hes.server.ota;

import com.hes.common.error.ErrorCode;
import com.hes.server.web.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class OtaJobService {
    private final OtaJobRepository repository;
    private final List<OtaPhaseHandler> handlers;

    public OtaJobService(OtaJobRepository repository, List<OtaPhaseHandler> handlers) {
        this.repository = repository;
        this.handlers = handlers;
    }

    @Transactional
    public OtaJobEntity create(String deviceId, String firmwareVersion, String url, String sha256) {
        OtaJobEntity job = new OtaJobEntity();
        job.setJobCode("OTA-" + UUID.randomUUID().toString().substring(0, 8));
        job.setDeviceId(deviceId);
        job.setFirmwareVersion(firmwareVersion);
        job.setPackageUrl(url);
        job.setPackageSha256(sha256);
        job.setPhase("CREATED");
        return repository.save(job);
    }

    @Transactional
    public OtaJobEntity transition(String jobCode, boolean downloadOk, boolean applyOk, boolean agentAck) {
        OtaJobEntity job = repository.findByJobCode(jobCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_FAILED, "ota job not found"));
        for (OtaPhaseHandler handler : handlers) {
            if (handler.canTransition(job.getPhase(), downloadOk, applyOk, agentAck)) {
                job.setPhase(handler.toPhase());
                return repository.save(job);
            }
        }
        throw new BusinessException(ErrorCode.VALIDATION_FAILED, "illegal ota transition from " + job.getPhase());
    }
}