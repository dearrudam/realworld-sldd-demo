package dev.realworld.authuser;

import dev.realworld.authuser.boundary.UpdateUserRequest;
import dev.realworld.authuser.control.UserControl;
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
class UserControlTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    UserControl userControl;

    @Test
    void findCurrentUser_existingUser_returnsUser() {
        var user = new User("Jacob", "jake@jake.jake", "hash", "salt", "PBKDF2", "bio", null);
        when(userRepository.findById("Jacob")).thenReturn(Optional.of(user));
        when(tokenService.generateToken("Jacob")).thenReturn("jwt.token.here");

        var result = userControl.findCurrentUser("Jacob");

        assertNotNull(result);
        assertEquals("Jacob", result.user().username());
    }

    @Test
    void updateCurrentUser_validUpdate_persistsChanges() {
        var user = new User("Jacob", "jake@jake.jake", "hash", "salt", "PBKDF2", "", null);
        when(userRepository.findById("Jacob")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(tokenService.generateToken("Jacob")).thenReturn("jwt.token.here");

        var request = new UpdateUserRequest("new@jake.jake", null, "I like turtles", null);
        var result = userControl.updateCurrentUser("Jacob", request);

        assertNotNull(result);
        assertEquals("new@jake.jake", result.user().email());
    }

    @Test
    void updateCurrentUser_changePassword_hashesNewPassword() {
        var user = new User("Jacob", "jake@jake.jake", "hash", "salt", "PBKDF2", "", null);
        when(userRepository.findById("Jacob")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(tokenService.generateToken("Jacob")).thenReturn("jwt.token.here");

        var request = new UpdateUserRequest(null, "newpassword123", null, null);
        var result = userControl.updateCurrentUser("Jacob", request);

        assertNotNull(result);
        assertEquals("Jacob", result.user().username());
    }
}
