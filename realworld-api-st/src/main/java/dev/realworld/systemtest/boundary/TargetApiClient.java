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

record NewUserRequest(UserRegistration user) {}

record UserRegistration(String username, String email, String password) {}

record LoginRequest(LoginUser user) {}

record LoginUser(String email, String password) {}

record UpdateUserRequest(UpdateUser user) {}

record UpdateUser(String email, String password, String bio, String image) {}

record UserResponse(UserDto user) {}

record UserDto(String email, String token, String username, String bio, String image) {}
