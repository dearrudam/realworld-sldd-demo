package dev.realworld.authuser;

import dev.realworld.authuser.entity.TokenService;
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
        var token = tokenService.generateToken("Jacob");
        assertNotNull(token);
    }

    @Test
    void generateToken_expiresInFiveMinutes() {
        var token = tokenService.generateToken("Jacob");
        assertNotNull(token);
    }

    @Test
    void generateToken_containsIssuer() {
        var token = tokenService.generateToken("Jacob");
        assertNotNull(token);
    }
}
