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
class UserControlTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordHasher passwordHasher;

    @InjectMocks
    UserControl userControl;

    @Test
    void findCurrentUser_existingUser_returnsUser() {
        User user = new User("testuser", "user@example.com", "hash", "salt", "PBKDF2WithHmacSHA256", "A bio", null);
        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));

        User result = userControl.findCurrentUser("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.username());
        assertEquals("user@example.com", result.email());
    }

    @Test
    void findCurrentUser_nonexistentUser_throwsException() {
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userControl.findCurrentUser("unknown"));
    }

    @Test
    void updateCurrentUser_validUpdate_persistsChanges() {
        User existing = new User("testuser", "old@example.com", "hash", "salt", "PBKDF2WithHmacSHA256", "old bio", null);
        when(userRepository.findById("testuser")).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userControl.updateCurrentUser("testuser", "new@example.com", "new bio", null, null);

        assertEquals("new@example.com", result.email());
        assertEquals("new bio", result.bio());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateCurrentUser_emailConflict_throwsException() {
        User existing = new User("testuser", "old@example.com", "hash", "salt", "PBKDF2WithHmacSHA256", null, null);
        User otherUser = new User("otheruser", "taken@example.com", "hash2", "salt2", "PBKDF2WithHmacSHA256", null, null);
        when(userRepository.findById("testuser")).thenReturn(Optional.of(existing));
        when(userRepository.findByEmail("taken@example.com")).thenReturn(Optional.of(otherUser));

        assertThrows(DuplicateEmailException.class, () -> userControl.updateCurrentUser("testuser", "taken@example.com", null, null, null));
    }

    @Test
    void updateCurrentUser_changePassword_hashesNewPassword() {
        User existing = new User("testuser", "user@example.com", "oldhash", "oldsalt", "PBKDF2WithHmacSHA256", null, null);
        when(userRepository.findById("testuser")).thenReturn(Optional.of(existing));
        when(passwordHasher.generateSalt()).thenReturn("newsalt");
        when(passwordHasher.hash("newpassword123", "newsalt")).thenReturn("newhash");
        when(passwordHasher.algorithm()).thenReturn("PBKDF2WithHmacSHA256");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userControl.updateCurrentUser("testuser", null, null, null, "newpassword123");

        assertEquals("newhash", result.passwordHash());
        assertEquals("newsalt", result.passwordSalt());
        assertEquals("PBKDF2WithHmacSHA256", result.passwordAlgorithm());
    }
}