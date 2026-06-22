package dev.realworld.auth.boundary;

import jakarta.json.bind.annotation.JsonbProperty;

public record UpdateUserRequest(
        @JsonbProperty("user") User user
) {
    public record User(
            String email,
            String username,
            String password,
            String bio,
            String image
    ) {
    }
}
