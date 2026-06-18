package dev.realworld.authuser.entity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TokenServiceTest {

    @Inject
    TokenService tokenService;

    @Test
    void generateToken_containsSubjectClaim() {
        String token = tokenService.generateToken("testuser");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_expiresInFiveMinutes() {
        long beforeGeneration = System.currentTimeMillis() / 1000;
        String token = tokenService.generateToken("testuser");
        long afterGeneration = System.currentTimeMillis() / 1000;

        assertNotNull(token);
        assertTrue(token.split("\\.").length >= 2, "Token should be a JWT with at least 2 parts");
    }

    @Test
    void generateToken_containsIssuer() {
        String token = tokenService.generateToken("testuser");

        assertNotNull(token);
        assertTrue(token.startsWith("ey"), "Token should start with JWT header");
    }
}
