package dev.realworld;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ApplicationShellHealthTest {

    @Test
    void healthEndpointsReportShellUp() {
        assertHealthEndpointIsUp("/q/health");
        assertHealthEndpointIsUp("/q/health/live");
        assertHealthEndpointIsUp("/q/health/ready");
    }

    @Test
    void generatedGreetingEndpointIsNotPublished() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(404);
    }

    void assertHealthEndpointIsUp(String path) {
        given()
                .when().get(path)
                .then()
                .statusCode(200)
                .body("status", is("UP"));
    }
}
