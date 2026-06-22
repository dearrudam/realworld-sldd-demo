package dev.realworld.auth.boundary;

import jakarta.json.bind.annotation.JsonbProperty;

public record LoginRequest(
        @JsonbProperty("user") User user
) {
    public record User(
            String email,
            String password
    ) {
    }
}
