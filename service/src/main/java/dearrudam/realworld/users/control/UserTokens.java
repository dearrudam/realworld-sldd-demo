package dearrudam.realworld.users.control;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserTokens {

    private static final String JWT_HEADER = encoded("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
    private static final String SIGNING_ALGORITHM = "HmacSHA256";
    private static final byte[] SIGNING_KEY = "realworld-users-demo-signing-key".getBytes(StandardCharsets.UTF_8);

    public String tokenFor(String username) {
        var payload = encoded("{\"sub\":\"" + encoded(username) + "\"}");
        var unsigned = JWT_HEADER + "." + payload;
        return unsigned + "." + signature(unsigned);
    }

    public String usernameFromCredential(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        if (!token.startsWith("Token ")) {
            return null;
        }
        return usernameFromJwt(token.substring("Token ".length()));
    }

    private String usernameFromJwt(String jwt) {
        var parts = jwt.split("\\.", -1);
        if (parts.length != 3) {
            return null;
        }
        var unsigned = parts[0] + "." + parts[1];
        if (!parts[0].equals(JWT_HEADER) || !parts[2].equals(signature(unsigned))) {
            return null;
        }
        return subject(parts[1]);
    }

    private String subject(String encodedPayload) {
        try {
            var payload = new String(Base64.getUrlDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
            var prefix = "\"sub\":\"";
            var start = payload.indexOf(prefix);
            if (start < 0) {
                return null;
            }
            var valueStart = start + prefix.length();
            var valueEnd = payload.indexOf('"', valueStart);
            return valueEnd < 0 ? null : decoded(payload.substring(valueStart, valueEnd));
        } catch (IllegalArgumentException _) {
            return null;
        }
    }

    private static String encoded(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decoded(String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }

    private static String signature(String value) {
        try {
            var mac = Mac.getInstance(SIGNING_ALGORITHM);
            mac.init(new SecretKeySpec(SIGNING_KEY, SIGNING_ALGORITHM));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("JWT signing is unavailable", exception);
        }
    }
}
