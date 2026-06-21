package dev.realworld.authuser.boundary;

public record UserResponse(UserDto user) {
    public static UserResponse from(UserDto user) { return new UserResponse(user); }
}
