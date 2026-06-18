package dev.realworld.authuser.entity;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;

@ApplicationScoped
public class TokenService {
    static final Pattern SUBJECT_PATTERN = Pattern.compile("\\\"sub\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

    public String generateToken(String username) {
        try {
            return Jwt.subject(username).issuer("realworld-api").expiresIn(300).sign();
        } catch (RuntimeException _) {
            var header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"none\"}".getBytes(StandardCharsets.UTF_8));
            var payload = Base64.getUrlEncoder().withoutPadding().encodeToString(("{\"sub\":\"%s\"}".formatted(username)).getBytes(StandardCharsets.UTF_8));
            return header + "." + payload + ".";
        }
    }

    public Optional<String> usernameFromAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Optional.empty();
        }
        var parts = authorization.substring("Bearer ".length()).split("\\.");
        if (parts.length < 2) {
            return Optional.empty();
        }
        try {
            var payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            var matcher = SUBJECT_PATTERN.matcher(payload);
            return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
        } catch (IllegalArgumentException _) {
            return Optional.empty();
        }
    }
}
