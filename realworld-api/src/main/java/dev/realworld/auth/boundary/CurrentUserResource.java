package dev.realworld.auth.boundary;

import dev.realworld.auth.control.TokenIssuer;
import dev.realworld.auth.control.UserRegistry;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.HashMap;

@Path("/api/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("user")
public class CurrentUserResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    UserRegistry registry;

    @Inject
    TokenIssuer issuer;

    @GET
    public Response getCurrentUser() {
        var email = jwt.getSubject();
        var user = registry.findByEmail(email);
        var token = issuer.createToken(user);
        var response = UserResponse.from(
                user.email(), token, user.username(), user.bio(), user.image()
        );
        return Response.ok(response).build();
    }

    @PUT
    public Response updateUser(UpdateUserRequest request) {
        var email = jwt.getSubject();
        var updates = new HashMap<String, Object>();
        if (request.user().email() != null) updates.put("email", request.user().email());
        if (request.user().username() != null) updates.put("username", request.user().username());
        if (request.user().password() != null) updates.put("password", request.user().password());
        if (request.user().bio() != null) updates.put("bio", request.user().bio());
        if (request.user().image() != null) updates.put("image", request.user().image());

        var user = registry.update(email, updates);
        var token = issuer.createToken(user);
        var response = UserResponse.from(
                user.email(), token, user.username(), user.bio(), user.image()
        );
        return Response.ok(response).build();
    }
}
