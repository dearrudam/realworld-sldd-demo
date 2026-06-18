package dev.realworld.authuser.boundary;

public record UserResponse(UserDto user) {
    public static UserResponse from(UserDto userDto) {
        return new UserResponse(userDto);
    }
}