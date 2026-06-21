package dev.realworld.authuser.boundary;

import dev.realworld.authuser.control.UserControl;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    UserControl userControl;

    @GET
    @RolesAllowed("**")
    public Response getCurrentUser() {
        var response = userControl.findCurrentUser(jwt.getSubject());
        return Response.ok(response).build();
    }

    @PUT
    @RolesAllowed("**")
    public Response updateCurrentUser(@Valid UpdateWrapperRequest request) {
        var response = userControl.updateCurrentUser(jwt.getSubject(), request.user());
        return Response.ok(response).build();
    }
}
