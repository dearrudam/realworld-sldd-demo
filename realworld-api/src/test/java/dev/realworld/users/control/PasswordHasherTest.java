package dev.realworld.users.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherTest {

    @Test
    void storesSaltedHashWithoutPlaintextPassword() {
        var hasher = new PasswordHasher();
        var credential = hasher.hash("babbage");

        assertTrue(hasher.matches("babbage", credential));
        assertFalse(hasher.matches("lovelace", credential));
        assertFalse(credential.passwordHash().contains("babbage"));
        assertFalse(credential.salt().contains("babbage"));
    }
}
