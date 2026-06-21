package dev.realworld.authuser.entity;

import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@ApplicationScoped
public class PasswordHasher {
    static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    static final int ITERATIONS = 65_536;
    static final int KEY_LENGTH = 256;
    static final SecureRandom RANDOM = new SecureRandom();

    public User hashPassword(User user, String password) {
        var salt = new byte[16];
        RANDOM.nextBytes(salt);
        return user
                .withPasswordSalt(Base64.getEncoder().encodeToString(salt))
                .withPasswordHash(hash(password, salt))
                .withPasswordAlgorithm(ALGORITHM);
    }

    public boolean matches(String password, User user) {
        var salt = Base64.getDecoder().decode(user.passwordSalt());
        return hash(password, salt).equals(user.passwordHash());
    }

    String hash(String password, byte[] salt) {
        try {
            var spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            return Base64.getEncoder().encodeToString(SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded());
        } catch (Exception exception) {
            throw new IllegalStateException("Could not hash password", exception);
        }
    }
}
