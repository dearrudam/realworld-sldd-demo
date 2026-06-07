package dev.realworld.users.control;

import dev.realworld.users.entity.UserAccount;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class UserRepository {

    ConcurrentHashMap<String, UserAccount> usersById = new ConcurrentHashMap<>();

    public UserAccount save(UserAccount account) {
        usersById.put(account.id, account);
        return account;
    }

    public Optional<UserAccount> findById(String id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<UserAccount> findByEmail(String email) {
        return users().stream().filter(user -> user.email.equalsIgnoreCase(email)).findFirst();
    }

    public boolean emailExists(String email) {
        return findByEmail(email).isPresent();
    }

    public boolean emailExistsForOtherUser(String email, String userId) {
        return users().stream().anyMatch(user -> user.email.equalsIgnoreCase(email) && !user.id.equals(userId));
    }

    public boolean usernameExists(String username) {
        return users().stream().anyMatch(user -> user.username.equalsIgnoreCase(username));
    }

    public boolean usernameExistsForOtherUser(String username, String userId) {
        return users().stream().anyMatch(user -> user.username.equalsIgnoreCase(username) && !user.id.equals(userId));
    }

    Collection<UserAccount> users() {
        return usersById.values();
    }
}
