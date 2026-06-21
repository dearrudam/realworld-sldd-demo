package dev.realworld.authuser;

import dev.realworld.authuser.entity.TokenService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenServiceTest {
    @Test
    void generateToken_containsSubjectClaim() {
        var tokens = new TokenService();
        var token = tokens.generateToken("duke");

        assertEquals("duke", tokens.usernameFromAuthorization("Bearer " + token).orElseThrow());
    }
}
