package dearrudam.realworld.users.entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

import jakarta.nosql.Column;
import jakarta.nosql.Embeddable;

@Embeddable(Embeddable.EmbeddableType.GROUPING)
public record PasswordCredential(@Column String salt, @Column String hash) {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static PasswordCredential fromPassword(String password) {
        var salt = new byte[16];
        RANDOM.nextBytes(salt);
        var encodedSalt = HexFormat.of().formatHex(salt);
        return new PasswordCredential(encodedSalt, hash(encodedSalt, password));
    }

    public boolean matches(String password) {
        return this.hash.equals(hash(this.salt, password));
    }

    private static String hash(String salt, String password) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest.digest());
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
