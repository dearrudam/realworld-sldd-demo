package dearrudam.realworld.users.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@RegisterRestClient(configKey = "base_uri")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UsersResourceClient {

    @POST
    @Path("users")
    Response register(JsonObject payload);

    @POST
    @Path("users/login")
    Response authenticate(JsonObject payload);

    @GET
    @Path("user")
    Response current(@HeaderParam("Authorization") String token);

    @PUT
    @Path("user")
    Response update(@HeaderParam("Authorization") String token, JsonObject payload);

    @GET
    @Path("profiles/{username}")
    Response profile(@HeaderParam("Authorization") String token, @PathParam("username") String username);

    @POST
    @Path("profiles/{username}/follow")
    Response follow(@HeaderParam("Authorization") String token, @PathParam("username") String username);

    @DELETE
    @Path("profiles/{username}/follow")
    Response unfollow(@HeaderParam("Authorization") String token, @PathParam("username") String username);
}
