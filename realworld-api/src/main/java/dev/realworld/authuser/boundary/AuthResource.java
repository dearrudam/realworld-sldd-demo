package dev.realworld.authuser.boundary;
import dev.realworld.authuser.control.AuthControl;import jakarta.inject.Inject;import jakarta.validation.Valid;import jakarta.ws.rs.*;import jakarta.ws.rs.core.*;
@Path("/api/users") @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
public class AuthResource { @Inject AuthControl control;
 @POST public Response register(@Valid RegistrationRequest request){var u=request.user(); return Response.status(Response.Status.CREATED).entity(UserResponse.from(control.register(u.username(),u.email(),u.password()))).build();}
 @POST @Path("/login") public UserResponse login(@Valid LoginWrapperRequest request){var u=request.user(); return UserResponse.from(control.login(u.email(),u.password()));}
}
