package dev.realworld.authuser.boundary;

import dev.realworld.authuser.control.DuplicateEmailException;
import dev.realworld.authuser.control.UserControl;
import dev.realworld.authuser.control.UserNotFoundException;
import dev.realworld.authuser.entity.User;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

@Path("/api/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserControl userControl;

    @Inject
    JsonWebToken jwt;

    @GET
    @Authenticated
    public Response getCurrentUser() {
        try {
            String username = jwt.getSubject();
            User user = userControl.findCurrentUser(username);
            UserDto userDto = new UserDto(user.email(), jwt.getRawToken(), user.username(), user.bio(), user.image());
            return Response.ok(new UserResponse(userDto)).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorResponse.of(java.util.List.of(e.getMessage())))
                    .build();
        }
    }

    @PUT
    @Authenticated
    public Response updateCurrentUser(@Valid UpdateWrapperRequest request) {
        try {
            String username = jwt.getName();
            User user = userControl.updateCurrentUser(
                    username,
                    request.user().email(),
                    request.user().bio(),
                    request.user().image(),
                    request.user().password()
            );
            UserDto userDto = new UserDto(user.email(), jwt.getRawToken(), user.username(), user.bio(), user.image());
            return Response.ok(new UserResponse(userDto)).build();
        } catch (DuplicateEmailException e) {
            return Response.status(422)
                    .entity(ErrorResponse.of(java.util.List.of(e.getMessage())))
                    .build();
        }
    }
}
