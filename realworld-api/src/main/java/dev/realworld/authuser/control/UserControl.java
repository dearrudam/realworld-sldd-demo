package dev.realworld.authuser.control;

import dev.realworld.authuser.entity.PasswordHasher;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserControl {

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordHasher passwordHasher;

    public User findCurrentUser(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    public User updateCurrentUser(String username, String email, String bio, String image, String password) {
        User existing = findCurrentUser(username);

        if (email != null && !email.equals(existing.email())) {
            userRepository.findByEmail(email).ifPresent(u -> {
                throw new DuplicateEmailException("email has already been taken");
            });
        }

        User updated = existing;
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
            String salt = passwordHasher.generateSalt();
            String hash = passwordHasher.hash(password, salt);
            String algorithm = passwordHasher.algorithm();
            updated = updated.withPasswordHash(hash)
                    .withPasswordSalt(salt)
                    .withPasswordAlgorithm(algorithm);
        }

        return userRepository.save(updated);
    }
}