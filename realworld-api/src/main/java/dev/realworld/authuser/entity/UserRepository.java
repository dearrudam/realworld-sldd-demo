package dev.realworld.authuser.entity;

import java.util.Optional;
import org.eclipse.jnosql.mapping.NoSQLRepository;
import jakarta.data.repository.Repository;

@Repository
public interface UserRepository extends NoSQLRepository<User, String> {
    Optional<User> findByEmail(String email);
}
