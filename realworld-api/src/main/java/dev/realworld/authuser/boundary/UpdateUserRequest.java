package dev.realworld.authuser.boundary;
import jakarta.validation.constraints.*;
public record UpdateUserRequest(@Email(message="email must be well-formed") String email,@Size(min=8,message="password must be at least 8 characters") String password,String bio,String image) {}
