package dev.realworld.authuser.boundary;
import dev.realworld.authuser.control.AuthenticatedUser;import dev.realworld.authuser.entity.User;
public record UserResponse(UserDto user) { static UserResponse from(AuthenticatedUser au){return from(au.user(),au.token());} static UserResponse from(User u,String token){return new UserResponse(new UserDto(u.email(),token,u.username(),u.bio(),u.image()));}}
