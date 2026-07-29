package com.hes.server.security.iam;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth")
public class AuthController {

    private final OpsAuthService opsAuthService;

    public AuthController(OpsAuthService opsAuthService) {
        this.opsAuthService = opsAuthService;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody RegisterOpsUserRequest request) {
        OpsUserEntity user = opsAuthService.register(request);
        return Map.of("username", user.getUsername(), "enabled", user.isEnabled());
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return opsAuthService.login(request);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("refreshToken is required");
        }
        return opsAuthService.refresh(refreshToken);
    }
}
