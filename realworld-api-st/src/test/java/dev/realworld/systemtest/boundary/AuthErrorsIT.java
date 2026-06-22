package dev.realworld.systemtest.boundary;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AuthErrorsIT {

    @RestClient
    UsersResourceClient client;

    @Test
    void loginReturns401ForInvalidCredentials() {
        var body = """
                {"user":{"email":"nonexistent@jake.jake","password":"WrongPass1"}}
                """;
        try {
            client.login(body);
            fail("Expected exception");
        } catch (WebApplicationException e) {
            assertEquals(401, e.getResponse().getStatus());
        }
    }

    @Test
    void getCurrentUserReturns401WithoutToken() {
        try {
            client.getCurrentUser(null);
            fail("Expected exception");
        } catch (WebApplicationException e) {
            assertEquals(401, e.getResponse().getStatus());
        }
    }

    @Test
    void updateUserReturns401WithoutToken() {
        var body = """
                {"user":{"bio":"no token update"}}
                """;
        try {
            client.updateUser(null, body);
            fail("Expected exception");
        } catch (WebApplicationException e) {
            assertEquals(401, e.getResponse().getStatus());
        }
    }

    @Test
    void registerReturns422ForDuplicateEmail() {
        var email = "duplicate-errors-" + System.currentTimeMillis() + "@jake.jake";
        var password = "JakeJake1";

        var registerBody = """
                {"user":{"username":"FirstUser","email":"%s","password":"%s"}}
                """.formatted(email, password);

        // First registration succeeds
        try (var response = client.register(registerBody)) {
            assertEquals(201, response.getStatus());
        }

        // Duplicate fails with 422
        var dupBody = """
                {"user":{"username":"SecondUser","email":"%s","password":"%s"}}
                """.formatted(email, password);
        try {
            client.register(dupBody);
            fail("Expected exception");
        } catch (WebApplicationException e) {
            assertEquals(422, e.getResponse().getStatus());
            var body = e.getResponse().readEntity(String.class);
            assertTrue(body.contains("errors"));
        }
    }

    @Test
    void registerReturns422ForWeakPassword() {
        var body = """
                {"user":{"username":"WeakPassUser","email":"weak@jake.jake","password":"a"}}
                """;
        try {
            client.register(body);
            fail("Expected exception");
        } catch (WebApplicationException e) {
            assertEquals(422, e.getResponse().getStatus());
            var bodyStr = e.getResponse().readEntity(String.class);
            assertTrue(bodyStr.contains("errors"));
        }
    }
}
