package dev.realworld.auth.boundary;

import dev.realworld.auth.control.TokenIssuer;
import dev.realworld.auth.control.UserRegistry;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject
    UserRegistry registry;

    @Inject
    TokenIssuer issuer;

    @POST
    public Response register(RegisterRequest request) {
        var user = registry.register(
                request.user().username(),
                request.user().email(),
                request.user().password()
        );
        var token = issuer.createToken(user);
        var response = UserResponse.from(
                user.email(), token, user.username(), user.bio(), user.image()
        );
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        var user = registry.login(
                request.user().email(),
                request.user().password()
        );
        var token = issuer.createToken(user);
        var response = UserResponse.from(
                user.email(), token, user.username(), user.bio(), user.image()
        );
        return Response.ok(response).build();
    }
}
