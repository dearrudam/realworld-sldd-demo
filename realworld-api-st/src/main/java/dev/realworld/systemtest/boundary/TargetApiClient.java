package dev.realworld.systemtest.boundary;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "service_uri")
@Path("/api")
@Consumes("application/json")
@Produces("application/json")
public interface TargetApiClient {
    @POST @Path("/users") UserResponse register(RegistrationRequest request);
    @POST @Path("/users/login") UserResponse login(LoginWrapperRequest request);
    @GET @Path("/user") UserResponse getCurrentUser(@HeaderParam("Authorization") String authorization);
    @PUT @Path("/user") UserResponse updateCurrentUser(@HeaderParam("Authorization") String authorization, UpdateWrapperRequest request);
}
