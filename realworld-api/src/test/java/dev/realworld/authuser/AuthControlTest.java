package dev.realworld.authuser;

import dev.realworld.authuser.boundary.NewUserRequest;
import dev.realworld.authuser.boundary.UserResponse;
import dev.realworld.authuser.control.AuthControl;
import dev.realworld.authuser.entity.DuplicateUserException;
import dev.realworld.authuser.entity.TokenService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControlTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    AuthControl authControl;

    @Test
    void register_validInput_createsUserWithHashedPassword() {
        when(userRepository.findById("Jacob")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("jake@jake.jake")).thenReturn(Optional.empty());
        when(tokenService.generateToken("Jacob")).thenReturn("jwt.token.here");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var request = new NewUserRequest("Jacob", "jake@jake.jake", "password123");
        var result = authControl.register(request);

        assertNotNull(result);
        assertNotNull(result.user());
        assertEquals("Jacob", result.user().username());
        assertEquals("jake@jake.jake", result.user().email());
        assertEquals("jwt.token.here", result.user().token());
    }

    @Test
    void register_duplicateEmail_throwsException() {
        var existing = new User("Other", "existing@jake.jake", "hash", "salt", "PBKDF2", "", null);
        when(userRepository.findById("Jacob")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("existing@jake.jake")).thenReturn(Optional.of(existing));

        var request = new NewUserRequest("Jacob", "existing@jake.jake", "password123");
        assertThrows(DuplicateUserException.class, () -> authControl.register(request));
    }

    @Test
    void register_duplicateUsername_throwsException() {
        var existing = new User("existinguser", "existing@jake.jake", "hash", "salt", "PBKDF2", "", null);
        when(userRepository.findById("existinguser")).thenReturn(Optional.of(existing));

        var request = new NewUserRequest("existinguser", "jake@jake.jake", "password123");
        assertThrows(DuplicateUserException.class, () -> authControl.register(request));
    }
}
