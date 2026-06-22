package dev.realworld.auth.entity;

import jakarta.data.repository.Repository;
import org.eclipse.jnosql.mapping.NoSQLRepository;

import java.util.Optional;

@Repository
public interface Users extends NoSQLRepository<User, String> {

    Optional<User> findByEmail(String email);
}
