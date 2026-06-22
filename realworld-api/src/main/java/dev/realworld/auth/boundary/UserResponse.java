package dev.realworld.auth.boundary;

import jakarta.json.bind.annotation.JsonbProperty;

public record UserResponse(
        @JsonbProperty("user") User user
) {
    public record User(
            String email,
            String token,
            String username,
            String bio,
            String image
    ) {
    }

    public static UserResponse from(String email, String token, String username, String bio, String image) {
        return new UserResponse(new User(email, token, username, bio, image));
    }
}
