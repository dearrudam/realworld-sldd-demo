package dev.realworld.users.control;

import dev.realworld.users.entity.UserAccount;

public record UserSession(UserAccount account, String token) {
}
