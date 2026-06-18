package dev.realworld.authuser.control;

import dev.realworld.authuser.entity.PasswordHasher;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControlTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordHasher passwordHasher;

    @InjectMocks
    AuthControl authControl;

    @Test
    void register_validInput_createsUserWithHashedPassword() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(userRepository.findById("testuser")).thenReturn(Optional.empty());
        when(passwordHasher.generateSalt()).thenReturn("c2FsdA==");
        when(passwordHasher.hash("password123", "c2FsdA==")).thenReturn("aGFzaA==");
        when(passwordHasher.algorithm()).thenReturn("PBKDF2WithHmacSHA256");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = authControl.register("testuser", "user@example.com", "password123");

        assertNotNull(result);
        assertEquals("testuser", result.username());
        assertEquals("user@example.com", result.email());
        assertEquals("aGFzaA==", result.passwordHash());
        assertEquals("c2FsdA==", result.passwordSalt());
        assertEquals("PBKDF2WithHmacSHA256", result.passwordAlgorithm());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throwsException() {
        User existing = new User("otheruser", "user@example.com", "hash", "salt", "PBKDF2WithHmacSHA256", null, null);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existing));

        assertThrows(DuplicateEmailException.class, () -> authControl.register("testuser", "user@example.com", "password123"));
    }

    @Test
    void register_duplicateUsername_throwsException() {
        User existing = new User("testuser", "other@example.com", "hash", "salt", "PBKDF2WithHmacSHA256", null, null);
        when(userRepository.findById("testuser")).thenReturn(Optional.of(existing));

        assertThrows(DuplicateUsernameException.class, () -> authControl.register("testuser", "user@example.com", "password123"));
    }

    @Test
    void login_validCredentials_returnsUser() {
        User existing = new User("testuser", "user@example.com", "aGFzaA==", "c2FsdA==", "PBKDF2WithHmacSHA256", "A bio", null);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existing));
        when(passwordHasher.verify("password123", "c2FsdA==", "aGFzaA==")).thenReturn(true);

        User result = authControl.login("user@example.com", "password123");

        assertNotNull(result);
        assertEquals("testuser", result.username());
    }

    @Test
    void login_wrongPassword_throwsException() {
        User existing = new User("testuser", "user@example.com", "aGFzaA==", "c2FsdA==", "PBKDF2WithHmacSHA256", null, null);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existing));
        when(passwordHasher.verify("wrongpassword", "c2FsdA==", "aGFzaA==")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authControl.login("user@example.com", "wrongpassword"));
    }

    @Test
    void login_unknownEmail_throwsException() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authControl.login("unknown@example.com", "password123"));
    }
}
