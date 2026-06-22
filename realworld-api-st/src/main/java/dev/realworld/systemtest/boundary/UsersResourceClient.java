package dev.realworld.systemtest.boundary;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "service_uri")
@Path("/api")
public interface UsersResourceClient {

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response register(String requestBody);

    @POST
    @Path("/users/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response login(String requestBody);

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    Response getCurrentUser(@HeaderParam("Authorization") String authorization);

    @PUT
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateUser(@HeaderParam("Authorization") String authorization, String requestBody);
}
