package dev.realworld.authuser.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class UserResourceIT {

    private String registerAndGetToken() {
        return registerAndGetToken("resuser" + System.nanoTime(), "res" + System.nanoTime() + "@example.com");
    }

    @Test
    void getCurrentUser_withValidToken_returns200WithUserEnvelope() {
        String username = "resuser" + System.nanoTime();
        String email = username + "@example.com";
        String token = registerAndGetToken(username, email);

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/api/user")
        .then()
            .statusCode(200)
            .body("user.username", equalTo(username))
            .body("user.email", equalTo(email))
            .body("user.token", notNullValue());
    }

    @Test
    void getCurrentUser_withoutToken_returns401() {
        given()
        .when()
            .get("/api/user")
        .then()
            .statusCode(401);
    }

    @Test
    void getCurrentUser_withExpiredToken_returns401() {
        String expiredToken = io.smallrye.jwt.build.Jwt.subject("testuser")
            .issuer("realworld-api")
            .expiresIn(java.time.Duration.ofMillis(-1))
            .signWithSecret("test-secret-key-for-realworld-api-min-32-chars!");

        given()
            .header("Authorization", "Bearer " + expiredToken)
        .when()
            .get("/api/user")
        .then()
            .statusCode(401);
    }

    @Test
    void updateCurrentUser_withValidToken_returns200WithUpdatedUser() {
        String token = registerAndGetToken();

        String updateBody = """
            { "user": { "bio": "Updated bio", "image": "https://example.com/img.png" } }
            """;

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(updateBody)
        .when()
            .put("/api/user")
        .then()
            .statusCode(200)
            .body("user.bio", equalTo("Updated bio"))
            .body("user.image", equalTo("https://example.com/img.png"));
    }

    @Test
    void updateCurrentUser_withoutToken_returns401() {
        String updateBody = """
            { "user": { "bio": "No auth" } }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(updateBody)
        .when()
            .put("/api/user")
        .then()
            .statusCode(401);
    }

    @Test
    void updateCurrentUser_duplicateEmail_returns422() {
        String suffix = String.valueOf(System.nanoTime());
        String token1 = registerAndGetToken("user1" + suffix, "email1" + suffix + "@example.com");
        String duplicateEmail = "email2" + suffix + "@example.com";

        String register2 = """
            { "user": { "username": "user2%s", "email": "%s", "password": "password123" } }
            """.formatted(suffix, duplicateEmail);
        given().contentType(ContentType.JSON).body(register2).post("/api/users");

        String updateBody = """
            { "user": { "email": "%s" } }
            """.formatted(duplicateEmail);

        given()
            .header("Authorization", "Bearer " + token1)
            .contentType(ContentType.JSON)
            .body(updateBody)
        .when()
            .put("/api/user")
        .then()
            .statusCode(422);
    }

    private String registerAndGetToken(String username, String email) {
        String body = String.format("""
            { "user": { "username": "%s", "email": "%s", "password": "password123" } }
            """, username, email);

        return given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .extract()
            .path("user.token");
    }
}
