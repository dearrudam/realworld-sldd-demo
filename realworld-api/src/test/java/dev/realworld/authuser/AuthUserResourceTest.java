package dev.realworld.authuser;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AuthUserResourceTest {
    @Test
    void register_newUser_returnsUserEnvelopeWithToken() {
        given().contentType("application/json")
                .body("""
                        {"user":{"username":"alicejwt","email":"alicejwt@example.com","password":"password123"}}
                        """)
                .when().post("/api/users")
                .then().statusCode(201)
                .body("user.email", equalTo("alicejwt@example.com"))
                .body("user.username", equalTo("alicejwt"))
                .body("user.token", not(blankOrNullString()));
    }

    @Test
    void login_validCredentials_returnsTokenAndCurrentUserCanBeRead() {
        var token = given().contentType("application/json")
                .body("{" + "\"user\":{\"username\":\"bobjwt\",\"email\":\"bobjwt@example.com\",\"password\":\"password123\"}}")
                .when().post("/api/users")
                .then().statusCode(201).extract().path("user.token").toString();

        given().contentType("application/json")
                .body("{" + "\"user\":{\"email\":\"bobjwt@example.com\",\"password\":\"password123\"}}")
                .when().post("/api/users/login")
                .then().statusCode(200)
                .body("user.username", equalTo("bobjwt"))
                .body("user.token", not(blankOrNullString()));

        given().header("Authorization", "Bearer " + token)
                .when().get("/api/user")
                .then().statusCode(200)
                .body("user.email", equalTo("bobjwt@example.com"));
    }

    @Test
    void updateCurrentUser_withValidToken_updatesFields() {
        var token = given().contentType("application/json")
                .body("{" + "\"user\":{\"username\":\"caroljwt\",\"email\":\"caroljwt@example.com\",\"password\":\"password123\"}}")
                .when().post("/api/users")
                .then().statusCode(201).extract().path("user.token").toString();

        given().contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body("{" + "\"user\":{\"email\":\"caroljwt2@example.com\",\"bio\":\"Writer\",\"image\":\"https://example.com/c.png\"}}")
                .when().put("/api/user")
                .then().statusCode(200)
                .body("user.email", equalTo("caroljwt2@example.com"))
                .body("user.bio", equalTo("Writer"))
                .body("user.image", equalTo("https://example.com/c.png"));
    }

    @Test
    void validationAndDuplicateFailures_returnRealWorldErrors() {
        given().contentType("application/json")
                .body("{" + "\"user\":{\"username\":\"bad-name\",\"email\":\"bad\",\"password\":\"short\"}}")
                .when().post("/api/users")
                .then().statusCode(422)
                .body("errors.body", not(empty()));

        var body = "{" + "\"user\":{\"username\":\"davejwt\",\"email\":\"davejwt@example.com\",\"password\":\"password123\"}}";
        given().contentType("application/json").body(body).when().post("/api/users").then().statusCode(201);
        given().contentType("application/json").body(body).when().post("/api/users").then().statusCode(422)
                .body("errors.body[0]", containsString("email"));
    }

    @Test
    void protectedEndpoints_withoutToken_return401() {
        given().when().get("/api/user").then().statusCode(401);
    }
}
