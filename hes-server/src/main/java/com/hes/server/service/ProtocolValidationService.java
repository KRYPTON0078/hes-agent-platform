package com.hes.server.service;

import com.hes.common.error.ErrorCode;
import com.hes.common.protocol.AgentMessage;
import com.hes.common.protocol.ProtocolVersions;
import com.hes.server.web.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class ProtocolValidationService {
    public void validateEnvelope(AgentMessage message) {
        if (message == null) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "message is required");
        }
        if (!ProtocolVersions.isSupported(message.protocolVersion())) {
            throw new BusinessException(ErrorCode.PROTOCOL_UNSUPPORTED,
                    "Unsupported protocolVersion: " + message.protocolVersion());
        }
        if (message.deviceId() == null || message.deviceId().isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "deviceId is required");
        }
        if (message.type() == null) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "type is required");
        }
    }
}
