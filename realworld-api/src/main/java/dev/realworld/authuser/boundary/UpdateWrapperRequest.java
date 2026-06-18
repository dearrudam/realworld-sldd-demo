package dev.realworld.authuser.boundary;

import jakarta.validation.Valid;

public record UpdateWrapperRequest(
    @Valid UpdateUserRequest user
) {}