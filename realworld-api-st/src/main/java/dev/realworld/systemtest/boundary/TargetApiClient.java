package dev.realworld.systemtest.boundary;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "service_uri")
@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface TargetApiClient {

    @POST
    @Path("/users")
    UserResponse register(NewUserRequest request);

    @POST
    @Path("/users/login")
    UserResponse login(LoginRequest request);

    @GET
    @Path("/user")
    UserResponse getCurrentUser(@HeaderParam("Authorization") String authHeader);

    @PUT
    @Path("/user")
    UserResponse updateCurrentUser(@HeaderParam("Authorization") String authHeader, UpdateUserRequest request);
}