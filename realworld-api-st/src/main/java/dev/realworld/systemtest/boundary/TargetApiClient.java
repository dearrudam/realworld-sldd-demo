package dev.realworld.systemtest.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "service_uri")
public interface TargetApiClient {
}
