package com.hes.server.security;

import com.hes.common.error.ErrorCode;
import com.hes.server.service.DeviceRegistryService;
import com.hes.server.web.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Enforces X-Api-Key on authenticated Agent routes after registration.
 */
@Component
public class AgentAuthInterceptor implements HandlerInterceptor {

    public static final String API_KEY_HEADER = "X-Api-Key";
    public static final String ATTR_DEVICE_ID = "hes.authenticatedDeviceId";

    private final DeviceRegistryService deviceRegistryService;

    public AgentAuthInterceptor(DeviceRegistryService deviceRegistryService) {
        this.deviceRegistryService = deviceRegistryService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        // Registration is the bootstrap call — no key yet.
        if ("POST".equalsIgnoreCase(request.getMethod()) && path.endsWith("/api/v1/agent/messages")) {
            // Auth is applied inside controller for non-register messages via DeviceRegistryService.assertApiKey
            return true;
        }
        if (path.matches(".*/api/v1/agent/[^/]+/commands")) {
            String deviceId = extractDeviceId(path);
            String apiKey = request.getHeader(API_KEY_HEADER);
            deviceRegistryService.assertApiKey(deviceId, apiKey);
            request.setAttribute(ATTR_DEVICE_ID, deviceId);
        }
        return true;
    }

    private static String extractDeviceId(String path) {
        String marker = "/api/v1/agent/";
        int start = path.indexOf(marker);
        if (start < 0) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid agent path");
        }
        String rest = path.substring(start + marker.length());
        int slash = rest.indexOf('/');
        if (slash <= 0) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid agent path");
        }
        return rest.substring(0, slash);
    }
}
