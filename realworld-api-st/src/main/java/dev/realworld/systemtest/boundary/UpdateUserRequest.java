package dev.realworld.systemtest.boundary;

public record UpdateUserRequest(User user) {
    public record User(String email, String password, String bio, String image) {}
}
