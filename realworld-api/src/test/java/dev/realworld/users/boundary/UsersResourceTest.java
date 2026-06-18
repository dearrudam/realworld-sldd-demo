package dev.realworld.users.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class UsersResourceTest {

    @Test
    void registersLogsInReadsAndUpdatesCurrentUser() {
        var email = "ada-%d@example.com".formatted(System.nanoTime());
        var username = "ada%d".formatted(System.nanoTime());
        var registration = """
                {"user":{"email":"%s","username":"%s","password":"babbage"}}
                """.formatted(email, username);

        var token = given().contentType("application/json").body(registration)
                .when().post("/api/users")
                .then().statusCode(201)
                .body("user.email", equalTo(email))
                .body("user.username", equalTo(username))
                .body("user.token", notNullValue())
                .extract().path("user.token").toString();

        given().contentType("application/json").body("""
                {"user":{"email":"%s","password":"babbage"}}
                """.formatted(email))
                .when().post("/api/users/login")
                .then().statusCode(200)
                .body("user.email", equalTo(email))
                .body("user.token", notNullValue());

        given().header("Authorization", "Bearer " + token)
                .when().get("/api/user")
                .then().statusCode(200)
                .body("user.email", equalTo(email));

        given().contentType("application/json").header("Authorization", "Bearer " + token).body("""
                {"user":{"bio":"first programmer","image":"https://example.com/ada.png"}}
                """)
                .when().put("/api/user")
                .then().statusCode(200)
                .body("user.bio", equalTo("first programmer"))
                .body("user.image", equalTo("https://example.com/ada.png"));
    }

    @Test
    void rejectsValidationDuplicateAndAuthenticationFailures() {
        var email = "grace-%d@example.com".formatted(System.nanoTime());
        var username = "grace%d".formatted(System.nanoTime());
        var body = """
                {"user":{"email":"%s","username":"%s","password":"hopper"}}
                """.formatted(email, username);

        given().contentType("application/json").body("""
                {"user":{"email":"invalid","username":"x","password":"1234"}}
                """).when().post("/api/users").then().statusCode(422);

        given().contentType("application/json").body(body).when().post("/api/users").then().statusCode(201);
        given().contentType("application/json").body(body).when().post("/api/users").then().statusCode(422);

        given().contentType("application/json").body("""
                {"user":{"email":"%s","password":"wrong"}}
                """.formatted(email)).when().post("/api/users/login").then().statusCode(401);

        given().when().get("/api/user").then().statusCode(401);
        given().header("Authorization", "Bearer invalid").when().get("/api/user").then().statusCode(401);
    }
}
