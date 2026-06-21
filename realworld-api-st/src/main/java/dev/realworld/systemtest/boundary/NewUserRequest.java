package dev.realworld.systemtest.boundary;

public record NewUserRequest(
        String username,
        String email,
        String password
) {
}
