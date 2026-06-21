package dev.realworld.systemtest.boundary;

import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "service_uri")
@Path("/api")
@Consumes("application/json")
@Produces("application/json")
public interface TargetApiClient {

    @POST
    @Path("/users")
    UserResponse register(UserRegistrationRequest request);

    @POST
    @Path("/users/login")
    UserResponse login(UserLoginRequest request);

    @GET
    @Path("/user")
    UserResponse getCurrentUser(@HeaderParam("Authorization") String authHeader);

    @PUT
    @Path("/user")
    UserResponse updateCurrentUser(@HeaderParam("Authorization") String authHeader, UserUpdateRequest request);
}
