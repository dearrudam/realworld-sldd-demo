package dev.realworld.systemtest.boundary;

public record UserResponse(User user) {
    public record User(String email, String token, String username, String bio, String image) {}
}
