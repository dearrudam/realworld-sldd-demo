package dev.realworld.authuser.entity;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.beans.ConstructorProperties;

@Entity("users")
public record User(
    @Id String username,
    @Column String email,
    @Column String passwordHash,
    @Column String passwordSalt,
    @Column String passwordAlgorithm,
    @Column String bio,
    @Column String image
) {
    @ConstructorProperties({"username", "email", "passwordHash", "passwordSalt", "passwordAlgorithm", "bio", "image"})
    public User {}

    public User withEmail(String email) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withPasswordHash(String passwordHash) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withPasswordSalt(String passwordSalt) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withPasswordAlgorithm(String passwordAlgorithm) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withBio(String bio) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withImage(String image) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }
}
