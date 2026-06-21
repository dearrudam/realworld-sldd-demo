package dev.realworld.authuser.boundary;

import dev.realworld.authuser.control.AuthControl;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
public class AuthResource {

    @Inject
    AuthControl authControl;

    @POST
    @PermitAll
    public Response register(@Valid RegistrationRequest request) {
        var response = authControl.register(request.user());
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginWrapperRequest request) {
        var response = authControl.login(request.user());
        return Response.ok(response).build();
    }
}
