package dev.realworld.auth.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PasswordHasher — PBKDF2 hashing and verification.
 */
class PasswordHasherTest {

    @Test
    void hashReturnsNonNullBase64EncodedString() {
        var hasher = new PasswordHasher();
        var hash = hasher.hash("MyPassword1");
        assertNotNull(hash);
        assertFalse(hash.isBlank());
        assertTrue(hash.contains(":"));
    }

    @Test
    void hashProducesDifferentResultsForSamePassword() {
        var hasher = new PasswordHasher();
        var hash1 = hasher.hash("MyPassword1");
        var hash2 = hasher.hash("MyPassword1");
        assertNotEquals(hash1, hash2);
    }

    @Test
    void verifyAcceptsCorrectPassword() {
        var hasher = new PasswordHasher();
        var password = "MyPassword1";
        var hash = hasher.hash(password);
        var salt = hasher.extractSalt(hash);
        assertTrue(hasher.verify(password, hash, salt));
    }

    @Test
    void verifyRejectsWrongPassword() {
        var hasher = new PasswordHasher();
        var hash = hasher.hash("MyPassword1");
        var salt = hasher.extractSalt(hash);
        assertFalse(hasher.verify("WrongPass1", hash, salt));
    }

    @Test
    void hashThrowsOnNullPassword() {
        var hasher = new PasswordHasher();
        assertThrows(NullPointerException.class, () -> hasher.hash(null));
    }
}
