package dev.realworld.authuser;

import dev.realworld.authuser.control.AuthControl;
import dev.realworld.authuser.entity.PasswordHasher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthControlTest {
    @Test
    void register_validInput_createsUserWithHashedPassword() {
        var user = new AuthControl(new FakeUserRepository(), new PasswordHasher())
                .register("unitduke", "unitduke@example.com", "correcthorsebattery");

        assertNotEquals("correcthorsebattery", user.passwordHash());
    }

    @Test
    void register_duplicateEmail_throwsException() {
        var control = new AuthControl(new FakeUserRepository(), new PasswordHasher());

        control.register("duke", "duke@example.com", "correcthorsebattery");

        assertThrows(RuntimeException.class, () -> control.register("duke2", "duke@example.com", "correcthorsebattery"));
    }
}
