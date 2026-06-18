package dev.realworld.authuser;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class UserResourceIT {
    @Test
    void getCurrentUser_withoutToken_returns401() {
        given()
                .when()
                .get("/api/user")
                .then()
                .statusCode(401);
    }

    @Test
    void updateCurrentUser_withValidToken_returns200WithUpdatedUser() {
        var token = given()
                .contentType("application/json")
                .body("""
                        {"user":{"username":"updateuser","email":"update@example.com","password":"correcthorsebattery"}}
                        """)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .path("user.token").toString();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("""
                        {"user":{"email":"updated@example.com","bio":"Quarkus fan","image":"https://example.com/duke.png"}}
                        """)
                .when()
                .put("/api/user")
                .then()
                .statusCode(200)
                .body("user.email", equalTo("updated@example.com"))
                .body("user.bio", equalTo("Quarkus fan"));
    }
}
