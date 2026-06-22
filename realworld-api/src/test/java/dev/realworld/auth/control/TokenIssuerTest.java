package dev.realworld.auth.control;

import dev.realworld.auth.entity.User;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TokenIssuer — RS256 JWT creation.
 */
class TokenIssuerTest {

    @Test
    void createTokenReturnsNonNullJwtString() {
        var issuer = new TokenIssuer(Duration.ofMinutes(5));
        var user = new User("id-1", "jake@jake.jake", "Jacob",
                "hash", "salt", null, null, null, null);
        var token = issuer.createToken(user);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void createTokenContainsThreeSegments() {
        var issuer = new TokenIssuer(Duration.ofMinutes(5));
        var user = new User("id-1", "jake@jake.jake", "Jacob",
                "hash", "salt", null, null, null, null);
        var token = issuer.createToken(user);
        var parts = token.split("\\.");
        assertEquals(3, parts.length);
    }
}
