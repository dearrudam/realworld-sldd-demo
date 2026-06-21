package dev.realworld.systemtest.boundary;

public record UpdateUserRequest(
        String email,
        String password,
        String bio,
        String image
) {
}
