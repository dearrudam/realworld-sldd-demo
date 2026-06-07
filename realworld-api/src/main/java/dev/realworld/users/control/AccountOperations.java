package dev.realworld.users.control;

import dev.realworld.users.entity.UserAccount;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class AccountOperations {

    @Inject UserRepository users;
    @Inject PasswordHasher passwords;
    @Inject TokenIssuer tokens;

    Clock clock = Clock.systemUTC();

    public UserSession register(String email, String username, String password) {
        ensureNewEmail(email);
        ensureNewUsername(username);
        var now = Instant.now(clock);
        var account = users.save(new UserAccount(UUID.randomUUID().toString(), email, username, passwords.hash(password), now));
        return session(account);
    }

    public UserSession login(String email, String password) {
        var account = users.findByEmail(email).orElseThrow(UserFailures.Authentication::new);
        if (!passwords.matches(password, account.credential)) {
            throw new UserFailures.Authentication();
        }
        return session(account);
    }

    public UserSession current(String token) {
        return session(accountFrom(token));
    }

    public UserSession update(String token, String email, String username, String password, String bio, String image) {
        var account = accountFrom(token);
        if (email != null && users.emailExistsForOtherUser(email, account.id)) {
            throw new UserFailures.Validation("email", "is already taken");
        }
        if (username != null && users.usernameExistsForOtherUser(username, account.id)) {
            throw new UserFailures.Validation("username", "is already taken");
        }
        account.update(email, username, password == null ? null : passwords.hash(password), bio, image, Instant.now(clock));
        users.save(account);
        return session(account);
    }

    UserAccount accountFrom(String token) {
        return users.findById(tokens.requireSubject(token)).orElseThrow(InvalidTokenException::new);
    }

    UserSession session(UserAccount account) {
        return new UserSession(account, tokens.issue(account));
    }

    void ensureNewEmail(String email) {
        if (users.emailExists(email)) {
            throw new UserFailures.Validation("email", "is already taken");
        }
    }

    void ensureNewUsername(String username) {
        if (users.usernameExists(username)) {
            throw new UserFailures.Validation("username", "is already taken");
        }
    }
}
