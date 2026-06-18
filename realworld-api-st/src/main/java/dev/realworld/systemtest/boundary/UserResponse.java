package dev.realworld.systemtest.boundary;

public record UserResponse(UserDto user) {
    public record UserDto(String email, String token, String username, String bio, String image) {}
}