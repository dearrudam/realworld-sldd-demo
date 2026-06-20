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
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/user")
@Authenticated
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject UserControl control;
    @Inject JsonWebToken jwt;

    @GET
    public UserResponse currentUser(@HeaderParam("Authorization") String authorization) {
        return UserResponse.from(control.findCurrentUser(jwt.getSubject()), bearerToken(authorization));
    }

    @PUT
    public UserResponse updateCurrentUser(@HeaderParam("Authorization") String authorization, @Valid UpdateWrapperRequest request) {
        var user = request.user();
        return UserResponse.from(
                control.updateCurrentUser(jwt.getSubject(), user.email(), user.bio(), user.image(), user.password()),
                bearerToken(authorization));
    }

    String bearerToken(String authorization) {
        return authorization == null || !authorization.startsWith("Bearer ") ? null : authorization.substring(7);
    }
}
