package dev.realworld.systemtest.boundary;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AuthScenarioIT {

    @RestClient
    UsersResourceClient client;

    @Test
    void registerLoginGetCurrentUserAndUpdate() {
        var username = "ScenarioUser_" + System.currentTimeMillis();
        var email = username.toLowerCase() + "@jake.jake";
        var password = "JakeJake1";

        // 1. Register
        var registerBody = """
                {"user":{"username":"%s","email":"%s","password":"%s"}}
                """.formatted(username, email, password);
        try (var response = client.register(registerBody)) {
            assertEquals(201, response.getStatus());
            var user = response.readEntity(String.class);
            assertTrue(user.contains(email));
            assertTrue(user.contains(username));
        }

        // 2. Login
        var loginBody = """
                {"user":{"email":"%s","password":"%s"}}
                """.formatted(email, password);
        String token;
        try (var response = client.login(loginBody)) {
            assertEquals(200, response.getStatus());
            var body = response.readEntity(String.class);
            assertTrue(body.contains(email));
            token = extractToken(body);
            assertNotNull(token);
        }

        // 3. Get current user
        try (var response = client.getCurrentUser("Bearer " + token)) {
            assertEquals(200, response.getStatus());
            var body = response.readEntity(String.class);
            assertTrue(body.contains(email));
        }

        // 4. Update user
        var newBio = "Updated bio for scenario test";
        var updateBody = """
                {"user":{"bio":"%s"}}
                """.formatted(newBio);
        try (var response = client.updateUser("Bearer " + token, updateBody)) {
            assertEquals(200, response.getStatus());
            var body = response.readEntity(String.class);
            assertTrue(body.contains(newBio));
        }
    }

    private String extractToken(String responseBody) {
        var tokenKey = "\"token\":\"";
        var start = responseBody.indexOf(tokenKey);
        if (start < 0) return null;
        start += tokenKey.length();
        var end = responseBody.indexOf("\"", start);
        return end > start ? responseBody.substring(start, end) : null;
    }
}
