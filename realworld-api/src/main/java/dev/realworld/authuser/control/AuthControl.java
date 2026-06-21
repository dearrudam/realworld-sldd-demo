package dev.realworld.authuser.control;

import dev.realworld.authuser.boundary.LoginRequest;
import dev.realworld.authuser.boundary.NewUserRequest;
import dev.realworld.authuser.boundary.UserDto;
import dev.realworld.authuser.boundary.UserResponse;
import dev.realworld.authuser.entity.DuplicateUserException;
import dev.realworld.authuser.entity.InvalidCredentialsException;
import dev.realworld.authuser.entity.TokenService;
import dev.realworld.authuser.entity.User;
import dev.realworld.authuser.entity.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@ApplicationScoped
public class AuthControl {

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    @Inject
    UserRepository userRepository;

    @Inject
    TokenService tokenService;

    public UserResponse register(NewUserRequest request) {
        if (userRepository.findById(request.username()).isPresent()) {
            throw new DuplicateUserException("username has already been taken");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateUserException("email has already been taken");
        }

        var salt = generateSalt();
        var hash = hashPassword(request.password(), salt);

        var user = new User(
                request.username(),
                request.email(),
                hash,
                Base64.getEncoder().encodeToString(salt),
                PBKDF2_ALGORITHM,
                "",
                null
        );
        userRepository.save(user);

        var token = tokenService.generateToken(user.username());
        var dto = new UserDto(user.email(), token, user.username(), user.bio(), user.image());
        return UserResponse.from(dto);
    }

    public UserResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("invalid credentials"));

        var salt = Base64.getDecoder().decode(user.passwordSalt());
        var hash = hashPassword(request.password(), salt);

        if (!hash.equals(user.passwordHash())) {
            throw new InvalidCredentialsException("invalid credentials");
        }

        var token = tokenService.generateToken(user.username());
        var dto = new UserDto(user.email(), token, user.username(), user.bio(), user.image());
        return UserResponse.from(dto);
    }

    private byte[] generateSalt() {
        var random = new SecureRandom();
        var salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) {
        try {
            var spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
            var factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            var key = factory.generateSecret(spec);
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }
}
