package dev.realworld.authuser.control;

import dev.realworld.authuser.boundary.NewUserRequest;
import dev.realworld.authuser.boundary.LoginRequest;
import dev.realworld.authuser.entity.TokenService;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthControl {
    @Inject UserRepository users;
    @Inject TokenService tokens;
    PasswordHashing passwords = new PasswordHashing();

    public AuthenticatedUser register(NewUserRequest request) {
        users.findById(request.username()).ifPresent(_ -> { throw new DuplicateUserException("username has already been taken"); });
        users.findByEmail(request.email()).ifPresent(_ -> { throw new DuplicateUserException("email has already been taken"); });
        var password = passwords.hash(request.password());
        var user = new User(request.username(), request.email(), password.hash(), password.salt(), password.algorithm(), "", null);
        users.save(user);
        return authenticated(user);
    }

    public AuthenticatedUser login(LoginRequest request) {
        var user = users.findByEmail(request.email()).orElseThrow(InvalidCredentialsException::new);
        if (!passwords.matches(request.password(), user.passwordHash(), user.passwordSalt())) {
            throw new InvalidCredentialsException();
        }
        return authenticated(user);
    }

    AuthenticatedUser authenticated(User user) {
        return new AuthenticatedUser(user, tokens.generateToken(user.username()));
    }
}
