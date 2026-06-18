package dev.realworld.authuser.control;

import dev.realworld.authuser.entity.PasswordHasher;
import dev.realworld.authuser.entity.TokenService;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthControl {

    @Inject
    UserRepository userRepository;

    @Inject
    TokenService tokenService;

    @Inject
    PasswordHasher passwordHasher;

    public User register(String username, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("email has already been taken");
        }
        if (userRepository.findById(username).isPresent()) {
            throw new DuplicateUsernameException("username has already been taken");
        }
        String salt = passwordHasher.generateSalt();
        String hash = passwordHasher.hash(password, salt);
        String algorithm = passwordHasher.algorithm();
        User user = new User(username, email, hash, salt, algorithm, null, null);
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("invalid credentials"));
        if (!passwordHasher.verify(password, user.passwordSalt(), user.passwordHash())) {
            throw new InvalidCredentialsException("invalid credentials");
        }
        return user;
    }

    public String generateToken(String username) {
        return tokenService.generateToken(username);
    }
}