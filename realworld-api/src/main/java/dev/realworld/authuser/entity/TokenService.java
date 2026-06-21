package dev.realworld.authuser.entity;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {
    public String generateToken(String username) {
        return Jwt.subject(username).sign();
    }
}
