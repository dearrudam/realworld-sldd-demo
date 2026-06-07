package dev.realworld.systemtest.boundary;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "service_uri")
@Path("/api")
public interface TargetApiClient {

    @POST
    @Path("/users")
    UserEnvelope register(UserRegistrationEnvelope request);

    @POST
    @Path("/users/login")
    UserEnvelope login(UserLoginEnvelope request);

    @GET
    @Path("/user")
    UserEnvelope current(@HeaderParam("Authorization") String authorization);

    @PUT
    @Path("/user")
    UserEnvelope update(@HeaderParam("Authorization") String authorization, UserUpdateEnvelope request);

    record UserRegistrationEnvelope(UserRegistration user) {
    }

    record UserRegistration(String email, String username, String password) {
    }

    record UserLoginEnvelope(UserLogin user) {
    }

    record UserLogin(String email, String password) {
    }

    record UserUpdateEnvelope(UserUpdate user) {
    }

    record UserUpdate(String email, String username, String password, String bio, String image) {
    }

    record UserEnvelope(UserRepresentation user) {
    }

    record UserRepresentation(String email, String token, String username, String bio, String image) {
    }
}
