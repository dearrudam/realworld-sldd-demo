package dev.realworld.authuser.boundary;

import dev.realworld.authuser.control.AuthControl;
import dev.realworld.authuser.control.DuplicateEmailException;
import dev.realworld.authuser.control.DuplicateUsernameException;
import dev.realworld.authuser.control.InvalidCredentialsException;
import dev.realworld.authuser.entity.User;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthControl authControl;

    @POST
    public Response register(@Valid RegistrationRequest request) {
        try {
            User user = authControl.register(request.user().username(), request.user().email(), request.user().password());
            String token = authControl.generateToken(user.username());
            UserDto userDto = new UserDto(user.email(), token, user.username(), user.bio(), user.image());
            return Response.status(Response.Status.CREATED)
                    .entity(new UserResponse(userDto))
                    .build();
        } catch (DuplicateEmailException e) {
            return Response.status(422)
                    .entity(ErrorResponse.of(java.util.List.of(e.getMessage())))
                    .build();
        } catch (DuplicateUsernameException e) {
            return Response.status(422)
                    .entity(ErrorResponse.of(java.util.List.of(e.getMessage())))
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginWrapperRequest request) {
        try {
            User user = authControl.login(request.user().email(), request.user().password());
            String token = authControl.generateToken(user.username());
            UserDto userDto = new UserDto(user.email(), token, user.username(), user.bio(), user.image());
            return Response.ok(new UserResponse(userDto)).build();
        } catch (InvalidCredentialsException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ErrorResponse.of(java.util.List.of(e.getMessage())))
                    .build();
        }
    }
}