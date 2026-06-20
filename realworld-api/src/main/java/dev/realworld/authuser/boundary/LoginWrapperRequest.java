package dev.realworld.authuser.boundary;
import jakarta.validation.Valid;import jakarta.validation.constraints.NotNull;
public record LoginWrapperRequest(@Valid @NotNull LoginRequest user) {}
