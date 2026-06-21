package dev.realworld.authuser.boundary;

import jakarta.validation.Valid;

public record RegistrationRequest(@Valid NewUserRequest user) {}
