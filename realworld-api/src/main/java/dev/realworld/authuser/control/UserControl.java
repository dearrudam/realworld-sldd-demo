package dev.realworld.authuser.control;

import dev.realworld.authuser.entity.DuplicateUserException;
import dev.realworld.authuser.entity.PasswordHasher;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserNotFoundException;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserControl {
    @Inject
    UserRepository users;

    @Inject
    PasswordHasher passwords;

    public User findCurrentUser(String username) {
        return users.findById(username).orElseThrow(UserNotFoundException::new);
    }

    public User updateCurrentUser(String username, String email, String password, String bio, String image) {
        var current = findCurrentUser(username);
        var updated = current;
        if (email != null) {
            users.findByEmail(email)
                    .filter(user -> !user.username().equals(username))
                    .ifPresent(existing -> {
                        throw new DuplicateUserException("email has already been taken");
                    });
            updated = updated.withEmail(email);
        }
        if (bio != null) {
            updated = updated.withBio(bio);
        }
        if (image != null) {
            updated = updated.withImage(image);
        }
        if (password != null) {
            updated = passwords.hashPassword(updated, password);
        }
        return users.save(updated);
    }
}
