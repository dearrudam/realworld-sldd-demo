package dev.realworld.systemtest.boundary;

public record LoginRequest(User user) {
    public record User(String email, String password) {}
}
