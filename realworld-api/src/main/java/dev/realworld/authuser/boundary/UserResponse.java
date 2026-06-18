package dev.realworld.authuser.boundary;

import dev.realworld.authuser.entity.User;

public record UserResponse(UserDto user) {
    public static UserResponse from(User user, String token) {
        return new UserResponse(new UserDto(user.email(), token, user.username(), user.bio(), user.image()));
    }
}
