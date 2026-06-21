package dev.realworld.authuser.boundary;

import dev.realworld.authuser.control.AuthControl;
import dev.realworld.authuser.control.AuthenticatedUser;
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
    @Inject AuthControl auth;

    @POST
    public Response register(@Valid RegistrationRequest request) {
        return Response.status(Response.Status.CREATED).entity(toResponse(auth.register(request.user()))).build();
    }

    @POST
    @Path("/login")
    public UserResponse login(@Valid LoginWrapperRequest request) {
        return toResponse(auth.login(request.user()));
    }

    static UserResponse toResponse(AuthenticatedUser authenticated) {
        var user = authenticated.user();
        return UserResponse.from(new UserDto(user.email(), authenticated.token(), user.username(), user.bio(), user.image()));
    }
}
