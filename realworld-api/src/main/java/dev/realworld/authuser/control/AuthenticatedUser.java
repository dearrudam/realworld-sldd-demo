package dev.realworld.authuser.control;

import dev.realworld.authuser.entity.User;

public record AuthenticatedUser(User user, String token) {}
