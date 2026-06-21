package dev.realworld.authuser.control;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

record PasswordHash(String hash, String salt, String algorithm) {}

class PasswordHashing {
    static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    static final int ITERATIONS = 65_536;
    static final int KEY_LENGTH = 256;
    SecureRandom random = new SecureRandom();

    PasswordHash hash(String password) {
        var salt = new byte[16];
        random.nextBytes(salt);
        return new PasswordHash(hash(password, salt), Base64.getEncoder().encodeToString(salt), ALGORITHM);
    }

    boolean matches(String password, String hash, String salt) {
        return hash(password, Base64.getDecoder().decode(salt)).equals(hash);
    }

    String hash(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            return Base64.getEncoder().encodeToString(SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded());
        } catch (Exception e) {
            throw new IllegalStateException("password hashing failed", e);
        }
    }
}
