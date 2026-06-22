package dev.realworld.auth.boundary;

import jakarta.json.bind.annotation.JsonbProperty;

public record RegisterRequest(
        @JsonbProperty("user") User user
) {
    public record User(
            String username,
            String email,
            String password
    ) {
    }
}
