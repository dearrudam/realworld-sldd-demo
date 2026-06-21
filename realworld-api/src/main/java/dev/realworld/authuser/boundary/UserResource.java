package dev.realworld.authuser.boundary;

import dev.realworld.authuser.control.UserControl;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/user")
@Consumes("application/json")
@Produces("application/json")
@Authenticated
public class UserResource {
    @Inject UserControl users;
    @Inject JsonWebToken jwt;

    @GET
    public UserResponse getCurrentUser(@HeaderParam("Authorization") String authorization) {
        return AuthResource.toResponse(users.findCurrentUser(jwt.getSubject(), token(authorization)));
    }

    @PUT
    public UserResponse updateCurrentUser(@HeaderParam("Authorization") String authorization, @Valid UpdateWrapperRequest request) {
        return AuthResource.toResponse(users.updateCurrentUser(jwt.getSubject(), token(authorization), request.user()));
    }

    String token(String authorization) {
        return authorization == null ? null : authorization.replaceFirst("^Bearer ", "");
    }
}
