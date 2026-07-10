package dearrudam.realworld.users.boundary;

import dearrudam.realworld.users.control.Users;
import dearrudam.realworld.users.entity.Profile;
import dearrudam.realworld.users.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
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
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject
    Users users;

    @POST
    @Path("users")
    public Response registerUser(JsonObject payload) {
        var user = payload.getJsonObject("user");
        return Response.status(Response.Status.CREATED)
                .entity(user(this.users.register(text(user, "username"), text(user, "email"), text(user, "password"))))
                .build();
    }

    @POST
    @Path("users/login")
    public Response authenticateUser(JsonObject payload) {
        var user = payload.getJsonObject("user");
        return Response.ok(user(this.users.authenticate(text(user, "email"), text(user, "password")))).build();
    }

    @GET
    @Path("user")
    public Response getCurrentUser(@HeaderParam("Authorization") String token) {
        return Response.ok(user(this.users.currentUser(token))).build();
    }

    @PUT
    @Path("user")
    public Response updateCurrentUser(@HeaderParam("Authorization") String token, JsonObject payload) {
        var user = payload.getJsonObject("user");
        return Response.ok(user(this.users.update(token, text(user, "username"), text(user, "email"), text(user, "password"), text(user, "bio"), text(user, "image")))).build();
    }

    @GET
    @Path("profiles/{username}")
    public Response viewProfile(@HeaderParam("Authorization") String token, @PathParam("username") String username) {
        return Response.ok(profile(this.users.profile(username, token))).build();
    }

    @POST
    @Path("profiles/{username}/follow")
    public Response followUser(@HeaderParam("Authorization") String token, @PathParam("username") String username) {
        return Response.ok(profile(this.users.follow(token, username))).build();
    }

    @jakarta.ws.rs.DELETE
    @Path("profiles/{username}/follow")
    public Response unfollowUser(@HeaderParam("Authorization") String token, @PathParam("username") String username) {
        return Response.ok(profile(this.users.unfollow(token, username))).build();
    }

    private JsonObject user(User user) {
        return Json.createObjectBuilder()
                .add("user", Json.createObjectBuilder()
                        .add("email", user.email())
                        .add("username", user.username())
                        .add("bio", value(user.bio()))
                        .add("image", value(user.image()))
                        .add("token", this.users.token(user)))
                .build();
    }

    private JsonObject profile(Profile profile) {
        return Json.createObjectBuilder()
                .add("profile", Json.createObjectBuilder()
                        .add("username", profile.username())
                        .add("bio", value(profile.bio()))
                        .add("image", value(profile.image()))
                        .add("following", profile.following()))
                .build();
    }

    private String text(JsonObject json, String name) {
        if (json == null || !json.containsKey(name) || json.isNull(name)) {
            return null;
        }
        return json.getString(name);
    }

    private String value(String value) {
        return value == null ? "" : value;
    }
}
