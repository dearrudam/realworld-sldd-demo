package dearrudam.realworld.users.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity
public class User {

    @Id
    private String username;
    @Column
    private String email;
    @Column
    private PasswordCredential passwordCredential;
    @Column
    private String bio;
    @Column
    private String image;
    @Column
    private Set<String> following = new LinkedHashSet<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.passwordCredential = PasswordCredential.fromPassword(password);
    }

    public String username() {
        return this.username;
    }

    public String getUsername() {
        return this.username;
    }

    public void username(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String email() {
        return this.email;
    }

    public String getEmail() {
        return this.email;
    }

    public void email(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean hasPassword(String password) {
        return this.passwordCredential.matches(password);
    }

    public void password(String password) {
        this.passwordCredential = PasswordCredential.fromPassword(password);
    }

    public PasswordCredential passwordCredential() {
        return this.passwordCredential;
    }

    public PasswordCredential getPasswordCredential() {
        return this.passwordCredential;
    }

    public void setPasswordCredential(PasswordCredential passwordCredential) {
        this.passwordCredential = passwordCredential;
    }

    public String bio() {
        return this.bio;
    }

    public String getBio() {
        return this.bio;
    }

    public void bio(String bio) {
        this.bio = bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String image() {
        return this.image;
    }

    public String getImage() {
        return this.image;
    }

    public void image(String image) {
        this.image = image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<String> getFollowing() {
        return this.following;
    }

    public void setFollowing(Set<String> following) {
        this.following = following;
    }

    public boolean follows(String username) {
        return this.following.contains(username);
    }

    public void follow(String username) {
        this.following.add(username);
    }

    public void unfollow(String username) {
        this.following.remove(username);
    }

    public boolean renameFollowing(String oldUsername, String newUsername) {
        if (this.following.remove(oldUsername)) {
            this.following.add(newUsername);
            return true;
        }
        return false;
    }
}
