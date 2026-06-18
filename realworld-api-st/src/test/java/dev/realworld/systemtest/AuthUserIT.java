package dev.realworld.systemtest;

import dev.realworld.systemtest.boundary.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AuthUserIT {

    @Inject
    @RestClient
    TargetApiClient client;

    @Test
    void register_newUser_returnsUserEnvelopeWithToken() {
        String username = "systestuser" + System.nanoTime();
        String email = username + "@example.com";
        NewUserRequest request = NewUserRequest.of(username, email, "password123");

        UserResponse response = client.register(request);

        assertNotNull(response);
        assertNotNull(response.user());
        assertEquals(username, response.user().username());
        assertEquals(email, response.user().email());
        assertNotNull(response.user().token());
    }

    @Test
    void register_duplicateEmail_returns422() {
        String suffix = String.valueOf(System.nanoTime());
        String email = "dup" + suffix + "@example.com";
        NewUserRequest request1 = NewUserRequest.of("dupuser1" + suffix, email, "password123");
        client.register(request1);

        NewUserRequest request2 = NewUserRequest.of("dupuser2" + suffix, email, "password123");

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            client.register(request2);
        });
        assertEquals(422, exception.getResponse().getStatus());
    }

    @Test
    void login_validCredentials_returnsUserEnvelopeWithToken() {
        String username = "loginuser" + System.nanoTime();
        String email = username + "@example.com";
        NewUserRequest regRequest = NewUserRequest.of(username, email, "password123");
        client.register(regRequest);

        LoginRequest loginRequest = LoginRequest.of(email, "password123");

        UserResponse response = client.login(loginRequest);

        assertNotNull(response);
        assertNotNull(response.user().token());
        assertEquals(username, response.user().username());
    }

    @Test
    void login_invalidPassword_returns401() {
        String username = "badpwuser" + System.nanoTime();
        String email = username + "@example.com";
        NewUserRequest regRequest = NewUserRequest.of(username, email, "password123");
        client.register(regRequest);

        LoginRequest loginRequest = LoginRequest.of(email, "wrongpassword");

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            client.login(loginRequest);
        });
        assertEquals(401, exception.getResponse().getStatus());
    }

    @Test
    void getCurrentUser_withValidToken_returnsUserEnvelope() {
        String username = "getuser" + System.nanoTime();
        String email = username + "@example.com";
        NewUserRequest regRequest = NewUserRequest.of(username, email, "password123");
        UserResponse regResponse = client.register(regRequest);
        String token = regResponse.user().token();

        UserResponse response = client.getCurrentUser("Bearer " + token);

        assertNotNull(response);
        assertEquals(username, response.user().username());
        assertEquals(email, response.user().email());
    }

    @Test
    void getCurrentUser_withoutToken_returns401() {
        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            client.getCurrentUser(null);
        });
        assertEquals(401, exception.getResponse().getStatus());
    }

    @Test
    void updateCurrentUser_withValidToken_updatesUserFields() {
        String username = "updateuser" + System.nanoTime();
        String email = username + "@example.com";
        NewUserRequest regRequest = NewUserRequest.of(username, email, "password123");
        UserResponse regResponse = client.register(regRequest);
        String token = regResponse.user().token();

        UpdateUserRequest updateRequest = UpdateUserRequest.of(null, "Updated bio", "https://example.com/img.png", null);

        UserResponse response = client.updateCurrentUser("Bearer " + token, updateRequest);

        assertNotNull(response);
        assertEquals("Updated bio", response.user().bio());
        assertEquals("https://example.com/img.png", response.user().image());
    }

    @Test
    void updateCurrentUser_duplicateEmail_returns422() {
        String suffix = String.valueOf(System.nanoTime());
        String emailA = "email_a" + suffix + "@example.com";
        String emailB = "email_b" + suffix + "@example.com";
        NewUserRequest reg1 = NewUserRequest.of("userA" + suffix, emailA, "password123");
        client.register(reg1);

        NewUserRequest reg2 = NewUserRequest.of("userB" + suffix, emailB, "password123");
        UserResponse reg2Response = client.register(reg2);
        String token2 = reg2Response.user().token();

        UpdateUserRequest updateRequest = UpdateUserRequest.of(emailA, null, null, null);

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            client.updateCurrentUser("Bearer " + token2, updateRequest);
        });
        assertEquals(422, exception.getResponse().getStatus());
    }
}
