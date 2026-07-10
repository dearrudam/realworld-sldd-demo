package dearrudam.realworld.users.control;

import java.util.Optional;

import dearrudam.realworld.users.entity.User;
import jakarta.data.repository.Repository;
import org.eclipse.jnosql.mapping.NoSQLRepository;

@Repository
public interface UserRecords extends NoSQLRepository<User, String> {

    Optional<User> findByEmail(String email);
}
