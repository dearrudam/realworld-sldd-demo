package dev.realworld.users.control;

import dev.realworld.users.entity.UserAccount;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@ApplicationScoped
public class TokenIssuer {

    @ConfigProperty(name = "realworld.jwt.issuer", defaultValue = "https://realworld.local")
    String issuer;

    @ConfigProperty(name = "realworld.jwt.secret")
    String secret;

    @ConfigProperty(name = "realworld.jwt.ttl", defaultValue = "PT2M")
    Duration ttl;

    Clock clock = Clock.systemUTC();

    public String issue(UserAccount account) {
        var now = Instant.now(clock);
        var header = Map.of("alg", "HS256", "typ", "JWT");
        var payload = Map.of(
                "iss", issuer,
                "sub", account.id,
                "username", account.username,
                "iat", now.getEpochSecond(),
                "exp", now.plus(ttl).getEpochSecond());
        var unsignedToken = "%s.%s".formatted(encodeJson(header), encodeJson(payload));
        return "%s.%s".formatted(unsignedToken, sign(unsignedToken));
    }

    public String requireSubject(String token) {
        var parts = token.split("\\.");
        if (parts.length != 3) {
            throw new InvalidTokenException();
        }
        var unsignedToken = "%s.%s".formatted(parts[0], parts[1]);
        if (!MessageDigestSupport.equal(sign(unsignedToken), parts[2])) {
            throw new InvalidTokenException();
        }
        var payload = JsonClaims.parse(new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8));
        if (!issuer.equals(payload.value("iss"))) {
            throw new InvalidTokenException();
        }
        if (Instant.now(clock).getEpochSecond() >= Long.parseLong(payload.value("exp"))) {
            throw new InvalidTokenException();
        }
        return payload.value("sub");
    }

    String encodeJson(Map<String, ?> claims) {
        var json = claims.entrySet().stream()
                .map(entry -> "\"%s\":%s".formatted(entry.getKey(), jsonValue(entry.getValue())))
                .reduce((left, right) -> left + "," + right)
                .orElse("");
        return Base64.getUrlEncoder().withoutPadding().encodeToString(("{" + json + "}").getBytes(StandardCharsets.UTF_8));
    }

    String jsonValue(Object value) {
        if (value instanceof Number number) {
            return number.toString();
        }
        return "\"%s\"".formatted(value.toString().replace("\\", "\\\\").replace("\"", "\\\""));
    }

    String sign(String value) {
        try {
            var mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }
}
