package dev.realworld.users.entity;

import java.util.Objects;

public record PasswordCredential(
        String salt,
        String passwordHash,
        String algorithm,
        int iterations,
        int keyLength) {

    public PasswordCredential {
        Objects.requireNonNull(salt);
        Objects.requireNonNull(passwordHash);
        Objects.requireNonNull(algorithm);
    }
}
