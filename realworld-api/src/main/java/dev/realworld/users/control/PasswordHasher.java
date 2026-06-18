package dev.realworld.users.control;

import dev.realworld.users.entity.PasswordCredential;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@ApplicationScoped
public class PasswordHasher {

    static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    static final int ITERATIONS = 120_000;
    static final int KEY_LENGTH = 256;
    static final int SALT_BYTES = 16;

    SecureRandom random = new SecureRandom();

    public PasswordCredential hash(String password) {
        var salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        return new PasswordCredential(encode(salt), encode(derive(password, salt, ITERATIONS, KEY_LENGTH)), ALGORITHM, ITERATIONS, KEY_LENGTH);
    }

    public boolean matches(String password, PasswordCredential credential) {
        var salt = Base64.getDecoder().decode(credential.salt());
        var candidate = derive(password, salt, credential.iterations(), credential.keyLength());
        var stored = Base64.getDecoder().decode(credential.passwordHash());
        return MessageDigest.isEqual(candidate, stored);
    }

    byte[] derive(String password, byte[] salt, int iterations, int keyLength) {
        try {
            var spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new CredentialHashingFailedException(e);
        }
    }

    String encode(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }
}
