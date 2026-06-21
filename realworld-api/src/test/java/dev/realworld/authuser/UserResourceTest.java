package dev.realworld.authuser;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    static String token;

    @Test
    @Order(0)
    void registerUser() {
        var response = given()
                .contentType("application/json")
                .body("""
                        { "user": { "username": "UserIT", "email": "userit@jake.jake", "password": "password123" } }
                        """)
                .when().post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .path("user.token");

        if (response instanceof String t) {
            token = t;
        }
        assertNotNull(token);
    }

    @Test
    @Order(1)
    void getCurrentUser_withValidToken_returns200WithUserEnvelope() {
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/api/user")
                .then()
                .statusCode(200)
                .body("user.username", is("UserIT"))
                .body("user.email", is("userit@jake.jake"));
    }

    @Test
    @Order(2)
    void getCurrentUser_withoutToken_returns401() {
        given()
                .when().get("/api/user")
                .then()
                .statusCode(401);
    }

    @Test
    @Order(3)
    void updateCurrentUser_withValidToken_returns200WithUpdatedUser() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body("""
                        { "user": { "bio": "I like turtles" } }
                        """)
                .when().put("/api/user")
                .then()
                .statusCode(200)
                .body("user.username", is("UserIT"))
                .body("user.bio", is("I like turtles"));
    }
}
