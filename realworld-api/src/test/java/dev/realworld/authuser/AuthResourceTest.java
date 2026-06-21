package dev.realworld.authuser;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthResourceTest {

    @Test
    @Order(1)
    void register_validInput_returns201WithUserEnvelope() {
        given()
                .contentType("application/json")
                .body("""
                        { "user": { "username": "JacobIT", "email": "jakeit@jake.jake", "password": "password123" } }
                        """)
                .when().post("/api/users")
                .then()
                .statusCode(201)
                .body("user.username", is("JacobIT"))
                .body("user.email", is("jakeit@jake.jake"));
    }

    @Test
    @Order(2)
    void register_duplicateEmail_returns422() {
        given()
                .contentType("application/json")
                .body("""
                        { "user": { "username": "Jacob2", "email": "jakeit@jake.jake", "password": "password123" } }
                        """)
                .when().post("/api/users")
                .then()
                .statusCode(422);
    }

    @Test
    @Order(3)
    void register_duplicateUsername_returns422() {
        given()
                .contentType("application/json")
                .body("""
                        { "user": { "username": "JacobIT", "email": "other@jake.jake", "password": "password123" } }
                        """)
                .when().post("/api/users")
                .then()
                .statusCode(422);
    }

    @Test
    @Order(4)
    void login_validCredentials_returns200WithToken() {
        given()
                .contentType("application/json")
                .body("""
                        { "user": { "email": "jakeit@jake.jake", "password": "password123" } }
                        """)
                .when().post("/api/users/login")
                .then()
                .statusCode(200)
                .body("user.email", is("jakeit@jake.jake"))
                .body("user.username", is("JacobIT"));
    }

    @Test
    @Order(5)
    void login_invalidCredentials_returns401() {
        given()
                .contentType("application/json")
                .body("""
                        { "user": { "email": "jakeit@jake.jake", "password": "wrongpassword" } }
                        """)
                .when().post("/api/users/login")
                .then()
                .statusCode(401);
    }
}
