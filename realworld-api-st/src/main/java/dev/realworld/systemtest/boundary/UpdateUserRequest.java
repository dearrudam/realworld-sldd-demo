package dev.realworld.systemtest.boundary;

public record UpdateUserRequest(UpdateUserDto user) {
    public static UpdateUserRequest of(String email, String bio, String image, String password) {
        return new UpdateUserRequest(new UpdateUserDto(email, bio, image, password));
    }

    public record UpdateUserDto(String email, String bio, String image, String password) {}
}