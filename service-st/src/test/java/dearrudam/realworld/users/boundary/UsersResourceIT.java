package dearrudam.realworld.users.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class UsersResourceIT {

    @Inject
    @RestClient
    UsersResourceClient users;

    @Test
    void accountLifecycleWorksThroughHttp() {
        var username = unique("neo");
        var email = username + "@zion.test";

        String token;
        try (var registration = this.users.register(user(username, email, "rabbit"))) {
            assertThat(registration.getStatus()).isEqualTo(201);
            token = userBody(registration).getString("token");
            assertThat(token.split("\\.", -1)).hasSize(3);
        }

        try (var login = this.users.authenticate(login(email, "rabbit"))) {
            assertThat(login.getStatus()).isEqualTo(200);
            assertThat(userBody(login).getString("username")).isEqualTo(username);
        }

        try (var current = this.users.current("Token " + token)) {
            assertThat(current.getStatus()).isEqualTo(200);
            assertThat(userBody(current).getString("email")).isEqualTo(email);
        }

        try (var update = this.users.update("Token " + token, update(null, null, null, "The One", null))) {
            assertThat(update.getStatus()).isEqualTo(200);
            assertThat(userBody(update).getString("bio")).isEqualTo("The One");
        }
    }

    @Test
    void profileFollowingWorksThroughHttp() {
        var follower = unique("trinity");
        var followed = unique("neo");
        var followerToken = register(follower);
        register(followed);

        try (var profile = this.users.profile(null, followed)) {
            assertThat(profile.getStatus()).isEqualTo(200);
            assertThat(profileBody(profile).getBoolean("following")).isFalse();
        }

        try (var follow = this.users.follow("Token " + followerToken, followed)) {
            assertThat(follow.getStatus()).isEqualTo(200);
            assertThat(profileBody(follow).getBoolean("following")).isTrue();
        }

        try (var unfollow = this.users.unfollow("Token " + followerToken, followed)) {
            assertThat(unfollow.getStatus()).isEqualTo(200);
            assertThat(profileBody(unfollow).getBoolean("following")).isFalse();
        }
    }

    @Test
    void invalidUserRequestsReturnErrors() {
        var username = unique("morpheus");
        register(username);

        try (var duplicate = this.users.register(user(username, unique("other") + "@zion.test", "rabbit"))) {
            assertThat(duplicate.getStatus()).isEqualTo(400);
        }

        try (var unauthorized = this.users.current(null)) {
            assertThat(unauthorized.getStatus()).isEqualTo(401);
        }

        try (var missingProfile = this.users.profile(null, unique("smith"))) {
            assertThat(missingProfile.getStatus()).isEqualTo(404);
        }
    }

    String register(String username) {
        try (var response = this.users.register(user(username, username + "@zion.test", "rabbit"))) {
            assertThat(response.getStatus()).isEqualTo(201);
            return userBody(response).getString("token");
        }
    }

    JsonObject user(String username, String email, String password) {
        return Json.createObjectBuilder()
                .add("user", Json.createObjectBuilder()
                        .add("username", username)
                        .add("email", email)
                        .add("password", password))
                .build();
    }

    JsonObject login(String email, String password) {
        return Json.createObjectBuilder()
                .add("user", Json.createObjectBuilder()
                        .add("email", email)
                        .add("password", password))
                .build();
    }

    JsonObject update(String username, String email, String password, String bio, String image) {
        var user = Json.createObjectBuilder();
        add(user, "username", username);
        add(user, "email", email);
        add(user, "password", password);
        add(user, "bio", bio);
        add(user, "image", image);
        return Json.createObjectBuilder().add("user", user).build();
    }

    JsonObject userBody(Response response) {
        return response.readEntity(JsonObject.class).getJsonObject("user");
    }

    JsonObject profileBody(Response response) {
        return response.readEntity(JsonObject.class).getJsonObject("profile");
    }

    String unique(String prefix) {
        return prefix + System.nanoTime();
    }

    void add(jakarta.json.JsonObjectBuilder builder, String name, String value) {
        if (value != null) {
            builder.add(name, value);
        }
    }
}
