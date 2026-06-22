package dev.realworld.auth.control;

import dev.realworld.auth.entity.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;

@ApplicationScoped
public class TokenIssuer {

    private final Duration expiry;

    public TokenIssuer(@ConfigProperty(name = "mp.jwt.token.expiry", defaultValue = "PT5M") Duration expiry) {
        this.expiry = expiry;
    }

    public String createToken(User user) {
        return Jwt.claims()
                .subject(user.email())
                .issuer("https://realworld.dev")
                .issuedAt(System.currentTimeMillis() / 1000)
                .expiresIn(expiry)
                .upn(user.email())
                .groups("user")
                .claim("email", user.email())
                .claim("username", user.username())
                .sign();
    }
}
