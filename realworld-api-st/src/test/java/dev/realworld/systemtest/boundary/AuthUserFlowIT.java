package dev.realworld.systemtest.boundary;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static dev.realworld.systemtest.boundary.TargetApiClient.UserLogin;
import static dev.realworld.systemtest.boundary.TargetApiClient.UserLoginEnvelope;
import static dev.realworld.systemtest.boundary.TargetApiClient.UserRegistration;
import static dev.realworld.systemtest.boundary.TargetApiClient.UserRegistrationEnvelope;
import static dev.realworld.systemtest.boundary.TargetApiClient.UserUpdate;
import static dev.realworld.systemtest.boundary.TargetApiClient.UserUpdateEnvelope;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class AuthUserFlowIT {

    @Inject
    @RestClient
    TargetApiClient targetApi;

    @Test
    void completesBlackBoxAuthUserFlow() {
        var unique = System.nanoTime();
        var email = "system-%d@example.com".formatted(unique);
        var username = "system%d".formatted(unique);

        var registered = targetApi.register(new UserRegistrationEnvelope(new UserRegistration(email, username, "babbage")));
        assertEquals(email, registered.user().email());
        assertNotNull(registered.user().token());

        var loggedIn = targetApi.login(new UserLoginEnvelope(new UserLogin(email, "babbage")));
        assertNotNull(loggedIn.user().token());

        var current = targetApi.current("Bearer " + loggedIn.user().token());
        assertEquals(username, current.user().username());

        var updated = targetApi.update("Bearer " + loggedIn.user().token(), new UserUpdateEnvelope(new UserUpdate(null, null, null, "system bio", null)));
        assertEquals("system bio", updated.user().bio());
    }

    @Test
    void rejectsInvalidToken() {
        var failure = assertThrows(WebApplicationException.class, () -> targetApi.current("Bearer invalid"));

        assertEquals(401, failure.getResponse().getStatus());
    }
}
