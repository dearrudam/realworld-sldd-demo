package dev.realworld.users.entity;

import java.time.Instant;
import java.util.Objects;

public final class UserAccount {

    public String id;
    public String email;
    public String username;
    public String bio;
    public String image;
    public PasswordCredential credential;
    public Instant createdAt;
    public Instant updatedAt;

    public UserAccount() {
    }

    public UserAccount(String id, String email, String username, PasswordCredential credential, Instant now) {
        this.id = Objects.requireNonNull(id);
        this.email = Objects.requireNonNull(email);
        this.username = Objects.requireNonNull(username);
        this.credential = Objects.requireNonNull(credential);
        this.createdAt = Objects.requireNonNull(now);
        this.updatedAt = now;
    }

    public void update(String email, String username, PasswordCredential credential, String bio, String image, Instant now) {
        if (email != null) {
            this.email = email;
        }
        if (username != null) {
            this.username = username;
        }
        if (credential != null) {
            this.credential = credential;
        }
        if (bio != null) {
            this.bio = bio;
        }
        if (image != null) {
            this.image = image;
        }
        this.updatedAt = Objects.requireNonNull(now);
    }
}
