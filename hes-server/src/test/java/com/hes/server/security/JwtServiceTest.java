package com.hes.server.security;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void roundTripsAccessTokenWithRoles() {
        JwtService service = new JwtService("local-dev-only-change-me-32b-min-secret!!", 900, 3600);
        String token = service.createAccessToken("alice", List.of("ADMIN", "OPERATOR"));
        JwtService.JwtPrincipal principal = service.parseAccessToken(token);
        assertEquals("alice", principal.username());
        assertTrue(principal.roles().contains("ADMIN"));
    }

    @Test
    void rejectsRefreshAsAccess() {
        JwtService service = new JwtService("local-dev-only-change-me-32b-min-secret!!", 900, 3600);
        String refresh = service.createRefreshToken("bob");
        assertThrows(IllegalArgumentException.class, () -> service.parseAccessToken(refresh));
    }
}
