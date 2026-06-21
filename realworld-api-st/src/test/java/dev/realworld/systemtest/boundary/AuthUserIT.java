package dev.realworld.systemtest.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class AuthUserIT {
    @RestClient TargetApiClient api;

    @Test
    void clientBeanIsAvailableForAuthUserScenarios() {
        assertNotNull(api);
    }
}
