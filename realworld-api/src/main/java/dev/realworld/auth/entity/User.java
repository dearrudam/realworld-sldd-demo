package dev.realworld.auth.entity;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.time.Instant;

@Entity
public record User(
        @Id String id,
        @Column String email,
        @Column String username,
        @Column String passwordHash,
        @Column String salt,
        @Column String bio,
        @Column String image,
        @Column Instant createdAt,
        @Column Instant updatedAt
) {
}
