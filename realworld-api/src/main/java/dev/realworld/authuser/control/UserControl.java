package dev.realworld.authuser.control;

import dev.realworld.authuser.boundary.UpdateUserRequest;
import dev.realworld.authuser.entity.TokenService;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserControl {
    @Inject UserRepository users;
    @Inject TokenService tokens;
    PasswordHashing passwords = new PasswordHashing();

    public AuthenticatedUser findCurrentUser(String username, String token) {
        return new AuthenticatedUser(users.findById(username).orElseThrow(CurrentUserNotFoundException::new), token);
    }

    public AuthenticatedUser updateCurrentUser(String username, String token, UpdateUserRequest request) {
        var user = users.findById(username).orElseThrow(CurrentUserNotFoundException::new);
        if (request.email() != null && !request.email().equals(user.email())) {
            users.findByEmail(request.email()).ifPresent(_ -> { throw new DuplicateUserException("email has already been taken"); });
            user = user.withEmail(request.email());
        }
        if (request.password() != null) {
            var password = passwords.hash(request.password());
            user = user.withPassword(password.hash(), password.salt(), password.algorithm());
        }
        if (request.bio() != null) user = user.withBio(request.bio());
        if (request.image() != null) user = user.withImage(request.image());
        users.save(user);
        return new AuthenticatedUser(user, token);
    }
}
