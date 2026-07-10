package dearrudam.realworld.users.entity;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity
public record Profile(@Id String username, @Column String bio, @Column String image, @Column boolean following) {
}
