package dev.realworld.authuser.control;

import dev.realworld.authuser.entity.DuplicateUserException;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserNotFoundException;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserControl {
    @Inject UserRepository repository;
    @Inject PasswordService passwords;

    public User findCurrentUser(String username) {
        return repository.findById(username).orElseThrow(UserNotFoundException::new);
    }

    public User updateCurrentUser(String username, String email, String bio, String image, String password) {
        var user = findCurrentUser(username);
        if (email != null && !email.equalsIgnoreCase(user.email())) {
            repository.findByEmail(email).ifPresent(_ -> { throw new DuplicateUserException("email has already been taken"); });
        }

        var updated = user;
        if (email != null) {
            updated = updated.withEmail(email);
        }
        if (bio != null) {
            updated = updated.withBio(bio);
        }
        if (image != null) {
            updated = updated.withImage(image);
        }
        if (password != null) {
            var salt = passwords.newSalt();
            updated = updated.withPassword(passwords.hash(password, salt), salt, passwords.algorithm());
        }
        return repository.save(updated);
    }
}
