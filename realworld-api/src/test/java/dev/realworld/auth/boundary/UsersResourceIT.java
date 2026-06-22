package dev.realworld.auth.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersResourceIT {

    @Test
    @Order(1)
    void registerReturns201ForValidPayload() {
        var body = """
                {
                  "user": {
                    "username": "Jacob",
                    "email": "jake@jake.jake",
                    "password": "JakeJake1"
                  }
                }
                """;
        given()
                .contentType("application/json")
                .body(body)
                .when().post("/api/users")
                .then()
                .statusCode(201)
                .body("user.email", equalTo("jake@jake.jake"))
                .body("user.username", equalTo("Jacob"))
                .body("user.token", notNullValue());
    }

    @Test
    @Order(2)
    void registerReturns422ForDuplicateEmail() {
        var body = """
                {
                  "user": {
                    "username": "Jacob2",
                    "email": "duplicate@jake.jake",
                    "password": "JakeJake1"
                  }
                }
                """;
        // First registration
        given()
                .contentType("application/json")
                .body(body)
                .when().post("/api/users");

        // Duplicate should fail
        var dupBody = """
                {
                  "user": {
                    "username": "Jacob3",
                    "email": "duplicate@jake.jake",
                    "password": "JakeJake1"
                  }
                }
                """;
        given()
                .contentType("application/json")
                .body(dupBody)
                .when().post("/api/users")
                .then()
                .statusCode(422)
                .body("errors", notNullValue());
    }

    @Test
    @Order(3)
    void registerReturns422ForWeakPassword() {
        var body = """
                {
                  "user": {
                    "username": "Jacob",
                    "email": "weak@jake.jake",
                    "password": "a"
                  }
                }
                """;
        given()
                .contentType("application/json")
                .body(body)
                .when().post("/api/users")
                .then()
                .statusCode(422)
                .body("errors", notNullValue());
    }

    @Test
    @Order(4)
    void loginReturns200ForValidCredentials() {
        // Register first
        var registerBody = """
                {
                  "user": {
                    "username": "LoginUser",
                    "email": "login@jake.jake",
                    "password": "JakeJake1"
                  }
                }
                """;
        given()
                .contentType("application/json")
                .body(registerBody)
                .when().post("/api/users");

        // Then login
        var loginBody = """
                {
                  "user": {
                    "email": "login@jake.jake",
                    "password": "JakeJake1"
                  }
                }
                """;
        given()
                .contentType("application/json")
                .body(loginBody)
                .when().post("/api/users/login")
                .then()
                .statusCode(200)
                .body("user.token", notNullValue());
    }

    @Test
    @Order(5)
    void loginReturns401ForInvalidCredentials() {
        var body = """
                {
                  "user": {
                    "email": "nonexistent@jake.jake",
                    "password": "WrongPass1"
                  }
                }
                """;
        given()
                .contentType("application/json")
                .body(body)
                .when().post("/api/users/login")
                .then()
                .statusCode(401)
                .body("errors", notNullValue());
    }

    @Test
    @Order(6)
    void getCurrentUserReturns401WithoutToken() {
        given()
                .when().get("/api/user")
                .then()
                .statusCode(401);
    }

    @Test
    @Order(7)
    void updateUserReturns401WithoutToken() {
        var body = """
                {
                  "user": {
                    "email": "jake@newdomain.io"
                  }
                }
                """;
        given()
                .contentType("application/json")
                .body(body)
                .when().put("/api/user")
                .then()
                .statusCode(401);
    }
}
