package dev.realworld.auth.control;

import dev.realworld.auth.boundary.AuthenticationException;
import dev.realworld.auth.boundary.RegistrationException;
import dev.realworld.auth.entity.User;
import dev.realworld.auth.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for UserRegistry — registration, login, and user management.
 */
class UserRegistryTest {

    private UserRegistry registry;

    @BeforeEach
    void setUp() {
        var hasher = new PasswordHasher();
        var issuer = new TokenIssuer(Duration.ofMinutes(5));
        registry = new UserRegistry();
        registry.hasher = hasher;
        registry.issuer = issuer;
        registry.users = new Users() {
            private User stored;

            @Override
            public <S extends User> S save(S entity) {
                stored = entity;
                return entity;
            }

            @Override
            public Optional<User> findById(String id) {
                return stored != null && stored.id().equals(id) ? Optional.of(stored) : Optional.empty();
            }

            @Override
            public Optional<User> findByEmail(String email) {
                return stored != null && stored.email().equals(email) ? Optional.of(stored) : Optional.empty();
            }

            @Override
            public void deleteAll() {
                stored = null;
            }

            @Override
            public long countBy() {
                return stored != null ? 1 : 0;
            }

            @Override
            public boolean existsById(String id) {
                return stored != null && stored.id().equals(id);
            }

            @Override
            public java.util.stream.Stream<User> findByIdIn(Iterable<String> ids) {
                return stored != null ? java.util.stream.Stream.of(stored) : java.util.stream.Stream.empty();
            }

            @Override
            public void deleteByIdIn(Iterable<String> ids) {
                stored = null;
            }

            @Override
            public <S extends User> java.util.List<S> saveAll(java.util.List<S> entities) {
                return entities;
            }

            @Override
            public java.util.stream.Stream<User> findAll() {
                return stored != null ? java.util.stream.Stream.of(stored) : java.util.stream.Stream.empty();
            }

            @Override
            public jakarta.data.page.Page<User> findAll(jakarta.data.page.PageRequest pageRequest, jakarta.data.Order<User> order) {
                return null;
            }

            @Override
            public void deleteById(String id) {
                stored = null;
            }

            @Override
            public void delete(User entity) {
                stored = null;
            }

            @Override
            public void deleteAll(java.util.List<? extends User> entities) {
                stored = null;
            }

            @Override
            public <S extends User> S insert(S entity) {
                stored = entity;
                return entity;
            }

            @Override
            public <S extends User> java.util.List<S> insertAll(java.util.List<S> entities) {
                return entities;
            }

            @Override
            public <S extends User> S update(S entity) {
                stored = entity;
                return entity;
            }

            @Override
            public <S extends User> java.util.List<S> updateAll(java.util.List<S> entities) {
                return entities;
            }
        };
    }

    @Test
    void registerCreatesAndReturnsUser() {
        var user = registry.register("Jacob", "jake@jake.jake", "JakeJake1");
        assertNotNull(user);
        assertEquals("jake@jake.jake", user.email());
        assertEquals("Jacob", user.username());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        registry.register("Jacob", "jake@jake.jake", "JakeJake1");
        assertThrows(RegistrationException.class,
                () -> registry.register("Jacob2", "jake@jake.jake", "JakeJake1"));
    }

    @Test
    void registerRejectsWeakPassword() {
        assertThrows(RegistrationException.class,
                () -> registry.register("Jacob", "jake@jake.jake", "a"));
    }

    @Test
    void loginWithValidCredentialsReturnsUser() {
        registry.register("Jacob", "jake@jake.jake", "JakeJake1");
        var user = registry.login("jake@jake.jake", "JakeJake1");
        assertNotNull(user);
        assertEquals("jake@jake.jake", user.email());
    }

    @Test
    void loginWithWrongPasswordThrows() {
        registry.register("Jacob", "jake@jake.jake", "JakeJake1");
        assertThrows(AuthenticationException.class,
                () -> registry.login("jake@jake.jake", "WrongPass1"));
    }
}
