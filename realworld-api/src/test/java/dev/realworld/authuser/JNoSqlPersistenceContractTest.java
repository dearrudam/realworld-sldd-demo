package dev.realworld.authuser;

import dev.realworld.authuser.control.AuthControl;
import dev.realworld.authuser.control.UserControl;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.data.repository.Repository;
import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import org.eclipse.jnosql.mapping.NoSQLRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JNoSqlPersistenceContractTest {
    @Test
    void user_isJNoSqlDocumentEntity() throws NoSuchFieldException {
        assertEquals("users", User.class.getAnnotation(Entity.class).value());
        assertNotNull(component("username").getAnnotation(Id.class));
        assertNotNull(component("email").getAnnotation(Column.class));
        assertNotNull(component("passwordHash").getAnnotation(Column.class));
        assertNotNull(component("passwordSalt").getAnnotation(Column.class));
        assertNotNull(component("passwordAlgorithm").getAnnotation(Column.class));
    }

    @Test
    void userRepository_isJakartaDataNoSqlRepository() {
        assertNotNull(UserRepository.class.getAnnotation(Repository.class));
        assertTrue(NoSQLRepository.class.isAssignableFrom(UserRepository.class));
    }

    @Test
    void controls_doNotDependOnInMemoryPersistence() throws ClassNotFoundException {
        assertFalse(dependsOnTypeNamed(AuthControl.class, "dev.realworld.authuser.entity.InMemoryUsers"));
        assertFalse(dependsOnTypeNamed(UserControl.class, "dev.realworld.authuser.entity.InMemoryUsers"));
        assertNotNull(Class.forName("dev.realworld.authuser.control.MongoIndexStartup"));
    }

    Field component(String name) throws NoSuchFieldException {
        return User.class.getDeclaredField(name);
    }

    boolean dependsOnTypeNamed(Class<?> type, String typeName) {
        return java.util.stream.Stream.of(type.getDeclaredFields())
                .map(Field::getType)
                .map(Class::getName)
                .anyMatch(typeName::equals);
    }
}
