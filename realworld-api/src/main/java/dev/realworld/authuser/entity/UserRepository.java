package dev.realworld.authuser.entity;

import jakarta.data.repository.Repository;
import org.eclipse.jnosql.mapping.NoSQLRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends NoSQLRepository<User, String> {
    Optional<User> findByEmail(String email);
}
