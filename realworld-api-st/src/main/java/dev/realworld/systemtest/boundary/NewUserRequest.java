package dev.realworld.systemtest.boundary;

public record NewUserRequest(User user) {
    public record User(String username, String email, String password) {}
}
