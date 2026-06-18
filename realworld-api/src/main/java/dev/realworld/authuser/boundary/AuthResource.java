package dev.realworld.authuser.boundary;

import dev.realworld.authuser.control.AuthControl;
import dev.realworld.authuser.entity.TokenService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
@Consumes("application/json")
@Produces("application/json")
public class AuthResource {
    @Inject
    AuthControl auth;

    @Inject
    TokenService tokens;

    @POST
    public Response register(@Valid RegistrationRequest request) {
        var user = auth.register(request.user().username(), request.user().email(), request.user().password());
        return Response.status(Response.Status.CREATED)
                .entity(UserResponse.from(user, tokens.generateToken(user.username())))
                .build();
    }

    @POST
    @Path("/login")
    public UserResponse login(@Valid LoginWrapperRequest request) {
        var user = auth.login(request.user().email(), request.user().password());
        return UserResponse.from(user, tokens.generateToken(user.username()));
    }
}
