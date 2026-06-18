package dev.realworld.systemtest.boundary;

public record LoginRequest(LoginDto user) {
    public static LoginRequest of(String email, String password) {
        return new LoginRequest(new LoginDto(email, password));
    }

    public record LoginDto(String email, String password) {}
}