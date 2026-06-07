package dev.realworld.systemtest.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TargetApiClientContractTest {

    @Test
    void registersWithSharedServiceUriConfigKey() {
        var registration = TargetApiClient.class.getAnnotation(RegisterRestClient.class);

        assertNotNull(registration);
        assertEquals("service_uri", registration.configKey());
    }
}
