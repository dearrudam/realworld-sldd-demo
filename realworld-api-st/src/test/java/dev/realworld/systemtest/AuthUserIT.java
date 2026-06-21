package dev.realworld.systemtest;

import dev.realworld.systemtest.boundary.LoginRequest;
import dev.realworld.systemtest.boundary.NewUserRequest;
import dev.realworld.systemtest.boundary.TargetApiClient;
import dev.realworld.systemtest.boundary.UpdateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class AuthUserIT {
    @RestClient
    TargetApiClient api;

    @Test
    void register_newUser_returnsUserEnvelopeWithToken() {
        var response = api.register(new NewUserRequest(new NewUserRequest.User("stduke", "stduke@example.com", "correcthorsebattery")));

        assertEquals("stduke@example.com", response.user().email());
        assertFalse(response.user().token().isBlank());
    }

    @Test
    void login_invalidPassword_returns401() {
        var exception = assertThrows(WebApplicationException.class,
                () -> api.login(new LoginRequest(new LoginRequest.User("stduke@example.com", "wrongpassword"))));

        assertEquals(401, exception.getResponse().getStatus());
    }

    @Test
    void updateCurrentUser_withValidToken_updatesUserFields() {
        var registration = api.register(new NewUserRequest(new NewUserRequest.User("stupdate", "stupdate@example.com", "correcthorsebattery")));
        var response = api.updateCurrentUser("Bearer " + registration.user().token(),
                new UpdateUserRequest(new UpdateUserRequest.User("stupdated@example.com", null, "Quarkus fan", null)));

        assertEquals("Quarkus fan", response.user().bio());
    }
}
