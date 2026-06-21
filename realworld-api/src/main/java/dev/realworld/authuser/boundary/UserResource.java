package dev.realworld.authuser.boundary;

import dev.realworld.authuser.control.UserControl;
import dev.realworld.authuser.entity.TokenService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/api/user")
@Consumes("application/json")
@Produces("application/json")
public class UserResource {
    @Inject
    UserControl users;

    @Inject
    TokenService tokens;

    @GET
    public UserResponse currentUser(@HeaderParam("Authorization") String authorization) {
        var username = tokens.usernameFromAuthorization(authorization).orElseThrow(() -> new NotAuthorizedException("Bearer"));
        return UserResponse.from(users.findCurrentUser(username), authorization.substring("Bearer ".length()));
    }

    @PUT
    public UserResponse updateCurrentUser(@HeaderParam("Authorization") String authorization, @Valid UpdateWrapperRequest request) {
        var username = tokens.usernameFromAuthorization(authorization).orElseThrow(() -> new NotAuthorizedException("Bearer"));
        var update = request.user();
        var user = users.updateCurrentUser(username, update.email(), update.password(), update.bio(), update.image());
        return UserResponse.from(user, authorization.substring("Bearer ".length()));
    }
}
