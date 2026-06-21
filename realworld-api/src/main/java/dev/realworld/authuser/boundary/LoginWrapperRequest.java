package dev.realworld.authuser.boundary;
import jakarta.validation.Valid;
public record LoginWrapperRequest(@Valid LoginRequest user) {}
