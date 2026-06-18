package dev.realworld.authuser.boundary;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewUserRequest(
    @NotBlank @Pattern(regexp = "^[a-zA-Z0-9]+$") String username,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password
) {}