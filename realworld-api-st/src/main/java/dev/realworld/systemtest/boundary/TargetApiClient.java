package dev.realworld.systemtest.boundary;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "service_uri")
public interface TargetApiClient {

    @GET
    @Path("/hello")
    String hello();
}
