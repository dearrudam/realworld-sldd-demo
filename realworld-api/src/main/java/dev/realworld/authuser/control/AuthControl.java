package dev.realworld.authuser.control;

import dev.realworld.authuser.entity.DuplicateUserException;
import dev.realworld.authuser.entity.InvalidCredentialsException;
import dev.realworld.authuser.entity.TokenService;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthControl {
    @Inject UserRepository repository;
    @Inject PasswordService passwords;
    @Inject TokenService tokens;

    public AuthenticatedUser register(String username, String email, String password) {
        repository.findById(username).ifPresent(_ -> { throw new DuplicateUserException("username has already been taken"); });
        repository.findByEmail(email).ifPresent(_ -> { throw new DuplicateUserException("email has already been taken"); });

        var salt = passwords.newSalt();
        var user = new User(username, email, passwords.hash(password, salt), salt, passwords.algorithm(), "", null);
        repository.save(user);
        return new AuthenticatedUser(user, tokens.generateToken(username));
    }

    public AuthenticatedUser login(String email, String password) {
        var user = repository.findByEmail(email).orElseThrow(InvalidCredentialsException::new);
        if (!passwords.matches(password, user.passwordSalt(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }
        return new AuthenticatedUser(user, tokens.generateToken(user.username()));
    }
}
