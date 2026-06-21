package dev.realworld.authuser;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;

@QuarkusTest
class AuthResourceIT {
    @Test
    void register_validInput_returns201WithUserEnvelope() {
        given()
                .contentType("application/json")
                .body("""
                        {"user":{"username":"duke","email":"duke@example.com","password":"correcthorsebattery"}}
                        """)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("user.email", org.hamcrest.Matchers.equalTo("duke@example.com"))
                .body("user.username", org.hamcrest.Matchers.equalTo("duke"))
                .body("user.token", not(blankOrNullString()));
    }

    @Test
    void register_invalidInput_returns422() {
        given()
                .contentType("application/json")
                .body("""
                        {"user":{"username":"duke!","email":"not-an-email","password":"short"}}
                        """)
                .when()
                .post("/api/users")
                .then()
                .statusCode(422)
                .body("errors.body", not(org.hamcrest.Matchers.empty()));
    }

    @Test
    void login_invalidCredentials_returns401() {
        given()
                .contentType("application/json")
                .body("""
                        {"user":{"email":"duke@example.com","password":"wrongpassword"}}
                        """)
                .when()
                .post("/api/users/login")
                .then()
                .statusCode(401);
    }
}
