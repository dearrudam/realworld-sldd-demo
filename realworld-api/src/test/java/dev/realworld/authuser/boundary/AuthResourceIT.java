package dev.realworld.authuser.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AuthResourceIT {

    @Test
    void register_validInput_returns201WithUserEnvelope() {
        String body = """
            { "user": { "username": "newuser", "email": "new@example.com", "password": "password123" } }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .body("user.username", equalTo("newuser"))
            .body("user.email", equalTo("new@example.com"))
            .body("user.token", notNullValue())
            .body("user.bio", nullValue())
            .body("user.image", nullValue());
    }

    @Test
    void register_duplicateEmail_returns422() {
        String body1 = """
            { "user": { "username": "user1", "email": "dup@example.com", "password": "password123" } }
            """;
        String body2 = """
            { "user": { "username": "user2", "email": "dup@example.com", "password": "password123" } }
            """;

        given().contentType(ContentType.JSON).body(body1).post("/api/users");

        given()
            .contentType(ContentType.JSON)
            .body(body2)
        .when()
            .post("/api/users")
        .then()
            .statusCode(422);
    }

    @Test
    void register_duplicateUsername_returns422() {
        String body1 = """
            { "user": { "username": "dupuser", "email": "first@example.com", "password": "password123" } }
            """;
        String body2 = """
            { "user": { "username": "dupuser", "email": "second@example.com", "password": "password123" } }
            """;

        given().contentType(ContentType.JSON).body(body1).post("/api/users");

        given()
            .contentType(ContentType.JSON)
            .body(body2)
        .when()
            .post("/api/users")
        .then()
            .statusCode(422);
    }

    @Test
    void register_invalidInput_returns422() {
        String body = """
            { "user": { "username": "!!!", "email": "not-an-email", "password": "short" } }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/api/users")
        .then()
            .statusCode(422);
    }

    @Test
    void login_validCredentials_returns200WithToken() {
        String registerBody = """
            { "user": { "username": "loginuser", "email": "login@example.com", "password": "password123" } }
            """;

        given().contentType(ContentType.JSON).body(registerBody).post("/api/users");

        String loginBody = """
            { "user": { "email": "login@example.com", "password": "password123" } }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(loginBody)
        .when()
            .post("/api/users/login")
        .then()
            .statusCode(200)
            .body("user.username", equalTo("loginuser"))
            .body("user.email", equalTo("login@example.com"))
            .body("user.token", notNullValue());
    }

    @Test
    void login_invalidCredentials_returns401() {
        String loginBody = """
            { "user": { "email": "nobody@example.com", "password": "password123" } }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(loginBody)
        .when()
            .post("/api/users/login")
        .then()
            .statusCode(401);
    }

    @Test
    void login_invalidInput_returns422() {
        String loginBody = """
            { "user": { "email": "not-an-email", "password": "" } }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(loginBody)
        .when()
            .post("/api/users/login")
        .then()
            .statusCode(422);
    }
}