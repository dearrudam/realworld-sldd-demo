package dev.realworld.auth.control;

import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@ApplicationScoped
public class PasswordHasher {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    public String hash(String password) {
        if (password == null) {
            throw new NullPointerException("password must not be null");
        }
        var salt = generateSalt();
        var hash = pbkdf2(password, salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public boolean verify(String password, String storedHash, String salt) {
        var saltBytes = Base64.getDecoder().decode(salt);
        var hash = pbkdf2(password, saltBytes);
        var parts = storedHash.split(":");
        if (parts.length < 2) {
            return false;
        }
        var storedHashBytes = Base64.getDecoder().decode(parts[1]);
        if (hash.length != storedHashBytes.length) {
            return false;
        }
        for (int i = 0; i < hash.length; i++) {
            if (hash[i] != storedHashBytes[i]) {
                return false;
            }
        }
        return true;
    }

    public String extractSalt(String storedHash) {
        var parts = storedHash.split(":");
        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid hash format");
        }
        return parts[0];
    }

    private byte[] generateSalt() {
        var random = new SecureRandom();
        var salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] pbkdf2(String password, byte[] salt) {
        try {
            var factory = SecretKeyFactory.getInstance(ALGORITHM);
            var spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("PBKDF2 algorithm not available", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Invalid key specification", e);
        }
    }
}
