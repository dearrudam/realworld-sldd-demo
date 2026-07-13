package dearrudam.realworld.users.control;

import dearrudam.realworld.users.entity.Profile;
import dearrudam.realworld.users.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class Users {

    @Inject
    UserRecords userRecords;

    @Inject
    UserTokens userTokens;

    public User register(String username, String email, String password) {
        requireValue(username, "username");
        requireValue(email, "email");
        requireValue(password, "password");
        requireAvailableUsername(username, null);
        requireAvailableEmail(email, null);

        var user = new User(username, email, password);
        this.userRecords.save(user);
        return user;
    }

    public User authenticate(String email, String password) {
        requireValue(email, "email");
        requireValue(password, "password");
        var user = this.userRecords.findByEmail(email).orElse(null);
        if (user == null || !user.hasPassword(password)) {
            throw new NotAuthorizedException("invalid credentials");
        }
        return user;
    }

    public User currentUser(String token) {
        var user = userFromCredential(token);
        if (user == null) {
            throw new NotAuthorizedException("valid credentials are required");
        }
        return user;
    }

    public User update(String token, String username, String email, String password, String bio, String image) {
        var user = currentUser(token);
        requireSuppliedValues(username, email, password, bio, image);
        if (username != null) {
            requireAvailableUsername(username, user);
        }
        if (email != null) {
            requireAvailableEmail(email, user);
        }

        var oldUsername = user.username();
        var oldEmail = user.email();
        if (username != null && !username.equals(oldUsername)) {
            this.userRecords.deleteById(oldUsername);
            user.username(username);
            this.userRecords.findAll().forEach(other -> {
                if (other.renameFollowing(oldUsername, username)) {
                    this.userRecords.save(other);
                }
            });
        }
        if (email != null && !email.equals(oldEmail)) {
            user.email(email);
        }
        if (password != null) {
            user.password(password);
        }
        if (bio != null) {
            user.bio(bio);
        }
        if (image != null) {
            user.image(image);
        }
        this.userRecords.save(user);
        return user;
    }

    public Profile profile(String username, String token) {
        requireValue(username, "username");
        var user = this.userRecords.findById(username).orElse(null);
        if (user == null) {
            throw new NotFoundException("profile not found");
        }
        var caller = userFromCredential(token);
        return profileFor(user, caller);
    }

    public Profile follow(String token, String username) {
        var caller = currentUser(token);
        var target = requireTarget(username, caller);
        caller.follow(target.username());
        this.userRecords.save(caller);
        return profileFor(target, caller);
    }

    public Profile unfollow(String token, String username) {
        var caller = currentUser(token);
        var target = requireTarget(username, caller);
        caller.unfollow(target.username());
        this.userRecords.save(caller);
        return profileFor(target, caller);
    }

    public String token(User user) {
        return this.userTokens.tokenFor(user.username());
    }

    public void reset() {
        this.userRecords.deleteAll();
    }

    private Profile profileFor(User user, User caller) {
        return new Profile(user.username(), user.bio(), user.image(), caller != null && caller.follows(user.username()));
    }

    private User requireTarget(String username, User caller) {
        requireValue(username, "username");
        var target = this.userRecords.findById(username).orElse(null);
        if (target == null || target.username().equals(caller.username())) {
            throw new BadRequestException("target user must exist and differ from caller");
        }
        return target;
    }

    private void requireAvailableUsername(String username, User current) {
        var existing = this.userRecords.findById(username).orElse(null);
        if (existing != null && existing != current) {
            throw new BadRequestException("username already used");
        }
    }

    private void requireAvailableEmail(String email, User current) {
        var existing = this.userRecords.findByEmail(email).orElse(null);
        if (existing != null && existing != current) {
            throw new BadRequestException("email already used");
        }
    }

    private void requireSuppliedValues(String... values) {
        for (var value : values) {
            if (value != null && value.isBlank()) {
                throw new BadRequestException("supplied values must be non-blank");
            }
        }
    }

    private void requireValue(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(name + " is required");
        }
    }

    private User userFromCredential(String token) {
        var username = this.userTokens.usernameFromCredential(token);
        return username == null ? null : this.userRecords.findById(username).orElse(null);
    }
}
