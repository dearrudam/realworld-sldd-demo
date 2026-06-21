package dev.realworld.systemtest;

import dev.realworld.systemtest.boundary.*;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusIntegrationTest
class AuthUserIT {

    @Inject
    @RestClient
    TargetApiClient targetApiClient;

    @Test
    void register_newUser_returnsUserEnvelopeWithToken() {
        var request = new UserRegistrationRequest(
                new NewUserRequest("Jacob", "jake@jake.jake", "password123"));
        var response = targetApiClient.register(request);
        assertNotNull(response);
    }

    @Test
    void login_validCredentials_returnsUserEnvelopeWithToken() {
        var request = new UserLoginRequest(
                new LoginRequest("jake@jake.jake", "password123"));
        var response = targetApiClient.login(request);
        assertNotNull(response);
    }

    @Test
    void getCurrentUser_withValidToken_returnsUserEnvelope() {
        var response = targetApiClient.getCurrentUser("Bearer valid.jwt.token");
        assertNotNull(response);
    }
}
