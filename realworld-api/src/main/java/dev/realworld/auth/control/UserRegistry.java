package dev.realworld.auth.control;

import dev.realworld.auth.boundary.AuthenticationException;
import dev.realworld.auth.boundary.RegistrationException;
import dev.realworld.auth.entity.User;
import dev.realworld.auth.entity.Users;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.*;

@ApplicationScoped
public class UserRegistry {

    @Inject
    Users users;

    @Inject
    PasswordHasher hasher;

    @Inject
    TokenIssuer issuer;

    public User register(String username, String email, String password) {
        var errors = new LinkedHashMap<String, List<String>>();

        if (username == null || username.isBlank()) {
            errors.computeIfAbsent("username", k -> new ArrayList<>()).add("can't be blank");
        }
        if (email == null || !email.contains("@") || email.isBlank()) {
            errors.computeIfAbsent("email", k -> new ArrayList<>()).add("is invalid");
        }
        var passwordErrors = validatePassword(password);
        if (!passwordErrors.isEmpty()) {
            errors.put("password", passwordErrors);
        }

        if (!errors.isEmpty()) {
            throw new RegistrationException(errors, 422);
        }

        if (users.findByEmail(email).isPresent()) {
            errors.computeIfAbsent("email", k -> new ArrayList<>()).add("is already taken");
            throw new RegistrationException(errors, 422);
        }

        var hash = hasher.hash(password);
        var salt = hasher.extractSalt(hash);
        var now = Instant.now();
        var user = new User(
                UUID.randomUUID().toString(),
                email,
                username,
                hash,
                salt,
                null,
                null,
                now,
                now
        );
        return users.save(user);
    }

    public User login(String email, String password) {
        if (email == null || password == null) {
            var errors = Map.of("email or password", List.of("is invalid"));
            throw new AuthenticationException(errors, 401);
        }

        var user = users.findByEmail(email)
                .orElseThrow(() -> {
                    var errors = Map.of("email or password", List.of("is invalid"));
                    return new AuthenticationException(errors, 401);
                });

        if (!hasher.verify(password, user.passwordHash(), user.salt())) {
            var errors = Map.of("email or password", List.of("is invalid"));
            throw new AuthenticationException(errors, 401);
        }

        return user;
    }

    public User findByEmail(String email) {
        return users.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(
                        Map.of("email", List.of("not found")), 404));
    }

    @SuppressWarnings("unchecked")
    public User update(String email, Map<String, Object> updates) {
        var user = findByEmail(email);
        var newEmail = (String) updates.getOrDefault("email", user.email());
        var newUsername = (String) updates.getOrDefault("username", user.username());
        var newBio = (String) updates.getOrDefault("bio", user.bio());
        var newImage = (String) updates.getOrDefault("image", user.image());
        var newPassword = (String) updates.get("password");

        String newHash = user.passwordHash();
        String newSalt = user.salt();
        if (newPassword != null && !newPassword.isBlank()) {
            newHash = hasher.hash(newPassword);
            newSalt = hasher.extractSalt(newHash);
        }

        var now = Instant.now();
        var updated = new User(
                user.id(),
                newEmail,
                newUsername,
                newHash,
                newSalt,
                newBio,
                newImage,
                user.createdAt(),
                now
        );
        return users.save(updated);
    }

    private List<String> validatePassword(String password) {
        var errors = new ArrayList<String>();
        if (password == null || password.length() < 8) {
            errors.add("must be at least 8 characters");
        }
        if (password != null && !password.chars().anyMatch(Character::isUpperCase)) {
            errors.add("must contain an uppercase letter");
        }
        if (password != null && !password.chars().anyMatch(Character::isDigit)) {
            errors.add("must contain a digit");
        }
        return errors;
    }
}
