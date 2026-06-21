package dev.realworld.authuser.boundary;
import jakarta.validation.constraints.*;
public record LoginRequest(@NotBlank @Email(message="email must be well-formed") String email,@NotBlank String password) {}
