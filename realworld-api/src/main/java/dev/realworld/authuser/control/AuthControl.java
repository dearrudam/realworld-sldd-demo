package dev.realworld.authuser.control;

import dev.realworld.authuser.entity.DuplicateUserException;
import dev.realworld.authuser.entity.InvalidCredentialsException;
import dev.realworld.authuser.entity.PasswordHasher;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthControl {
    @Inject
    UserRepository users;

    @Inject
    PasswordHasher passwords;

    AuthControl() {
    }

    public AuthControl(UserRepository users, PasswordHasher passwords) {
        this.users = users;
        this.passwords = passwords;
    }

    public User register(String username, String email, String password) {
        users.findById(username).ifPresent(existing -> {
            throw new DuplicateUserException("username has already been taken");
        });
        users.findByEmail(email).ifPresent(existing -> {
            throw new DuplicateUserException("email has already been taken");
        });
        return users.save(passwords.hashPassword(User.newUser(username, email), password));
    }

    public User login(String email, String password) {
        var user = users.findByEmail(email).orElseThrow(InvalidCredentialsException::new);
        if (!passwords.matches(password, user)) {
            throw new InvalidCredentialsException();
        }
        return user;
    }
}
