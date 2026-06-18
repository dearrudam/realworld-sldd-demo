package dev.realworld.authuser.boundary;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @Email String email,
    @Size(min = 8) String password,
    String bio,
    String image
) {}