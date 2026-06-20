package dev.realworld.authuser.entity;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity("users")
public record User(
        @Id String username,
        @Column String email,
        @Column String passwordHash,
        @Column String passwordSalt,
        @Column String passwordAlgorithm,
        @Column String bio,
        @Column String image) {

    public User withEmail(String email) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withPassword(String hash, String salt, String algorithm) {
        return new User(username, email, hash, salt, algorithm, bio, image);
    }

    public User withBio(String bio) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withImage(String image) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }
}
