package org.soujava.demo.sldd;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Path;

/**
 * Placeholder REST client for future black-box RealWorld API system tests.
 */
@Path("/")
@RegisterRestClient(configKey = "service_uri")
public interface RealWorldApiClient {
}
