package dev.realworld.systemtest.control;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationPropertiesContractTest {

    @Test
    void bridgesTargetApiBaseUrlToSharedRestClientConfigKey() throws IOException {
        var properties = new Properties();

        try (var reader = Files.newBufferedReader(Path.of("src/main/resources/application.properties"))) {
            properties.load(reader);
        }

        assertEquals("http://localhost:8080", properties.getProperty("realworld-api.base-url"));
        assertEquals("${realworld-api.base-url}", properties.getProperty("quarkus.rest-client.service_uri.url"));
    }
}
