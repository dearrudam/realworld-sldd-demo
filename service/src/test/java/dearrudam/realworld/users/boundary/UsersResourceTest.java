package dearrudam.realworld.users.boundary;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import dearrudam.realworld.users.control.Users;
import dearrudam.realworld.users.control.UserRecords;
import dearrudam.realworld.users.entity.PasswordCredential;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
class UsersResourceTest {

    @Inject
    UsersResource usersResource;

    @Inject
    Users users;

    @Inject
    UserRecords userRecords;

    @BeforeEach
    void resetUsers() {
        this.users.reset();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("registrationRequirements")
    void registerUser(String requirement, Executable requirementCheck) {
        assertDoesNotThrow(requirementCheck, requirement);
    }

    Stream<Arguments> registrationRequirements() {
        return Stream.of(
                Arguments.of("R1.1", (Executable) () -> {
                    var response = this.usersResource.registerUser(user("trinity", "trinity@zion.test", "rabbit"));
                    assertEquals(201, response.getStatus());
                    assertEquals("trinity", body(response).getJsonObject("user").getString("username"));
                }),
                Arguments.of("R1.2", (Executable) () -> assertThrows(BadRequestException.class,
                        () -> this.usersResource.registerUser(user(null, "neo@zion.test", "rabbit")))),
                Arguments.of("R1.3", (Executable) () -> assertThrows(BadRequestException.class,
                        () -> this.usersResource.registerUser(user(" ", "neo@zion.test", "rabbit")))),
                Arguments.of("R1.4", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.registerUser(user("neo", "other@zion.test", "rabbit")));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.registerUser(user("other", "neo@zion.test", "rabbit")));
                }),
                Arguments.of("R1.5", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    var credential = this.users.authenticate("neo@zion.test", "rabbit").passwordCredential();
                    assertCredentialMaterial(credential, "rabbit");
                }),
                Arguments.of("R1.6", (Executable) () -> {
                    var response = this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    assertJwt(body(response).getJsonObject("user").getString("token"));
                }),
                Arguments.of("R1.7", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    var stored = this.userRecords.findById("neo").orElseThrow();
                    assertEquals("neo@zion.test", stored.email());
                    assertCredentialMaterial(stored.passwordCredential(), "rabbit");
                }));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("authenticationRequirements")
    void authenticateUser(String requirement, Executable requirementCheck) {
        assertDoesNotThrow(requirementCheck, requirement);
    }

    Stream<Arguments> authenticationRequirements() {
        return Stream.of(
                Arguments.of("R2.1", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    var response = this.usersResource.authenticateUser(login("neo@zion.test", "rabbit"));
                    assertEquals(200, response.getStatus());
                }),
                Arguments.of("R2.2", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    assertThrows(NotAuthorizedException.class,
                            () -> this.usersResource.authenticateUser(login("neo@zion.test", "wrong")));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.authenticateUser(login("", "rabbit")));
                }),
                Arguments.of("R2.3", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    var response = this.usersResource.authenticateUser(login("neo@zion.test", "rabbit"));
                    assertJwt(body(response).getJsonObject("user").getString("token"));
                }));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("currentUserRequirements")
    void getCurrentUser(String requirement, Executable requirementCheck) {
        assertDoesNotThrow(requirementCheck, requirement);
    }

    Stream<Arguments> currentUserRequirements() {
        return Stream.of(
                Arguments.of("R3.1", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    var response = this.usersResource.getCurrentUser(authToken("neo"));
                    assertEquals("neo", body(response).getJsonObject("user").getString("username"));
                }),
                Arguments.of("R3.2", (Executable) () -> assertThrows(NotAuthorizedException.class,
                        () -> this.usersResource.getCurrentUser(null))),
                Arguments.of("R3.3", (Executable) () -> assertThrows(NotAuthorizedException.class,
                        () -> this.usersResource.getCurrentUser("Token not-a-jwt"))));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("updateRequirements")
    void updateCurrentUser(String requirement, Executable requirementCheck) {
        assertDoesNotThrow(requirementCheck, requirement);
    }

    Stream<Arguments> updateRequirements() {
        return Stream.of(
                Arguments.of("R4.1", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    var response = this.usersResource.updateCurrentUser(authToken("neo"), update("one", null, null, "chosen", null));
                    var user = body(response).getJsonObject("user");
                    assertEquals("one", user.getString("username"));
                    assertEquals("neo@zion.test", user.getString("email"));
                    assertEquals("chosen", user.getString("bio"));
                }),
                Arguments.of("R4.2", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.updateCurrentUser(authToken("neo"), update("", null, null, null, null)));
                }),
                Arguments.of("R4.3", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.registerUser(user("trinity", "trinity@zion.test", "rabbit"));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.updateCurrentUser(authToken("neo"), update("trinity", null, null, null, null)));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.updateCurrentUser(authToken("neo"), update(null, "trinity@zion.test", null, null, null)));
                }),
                Arguments.of("R4.4", (Executable) () -> assertThrows(NotAuthorizedException.class,
                        () -> this.usersResource.updateCurrentUser(null, update("neo", null, null, null, null)))),
                Arguments.of("R4.5", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.updateCurrentUser(authToken("neo"), update(null, null, "white-rabbit", null, null));

                    assertThrows(NotAuthorizedException.class,
                            () -> this.users.authenticate("neo@zion.test", "rabbit"));
                    var credential = this.users.authenticate("neo@zion.test", "white-rabbit").passwordCredential();
                    assertCredentialMaterial(credential, "white-rabbit");
                }),
                Arguments.of("R4.6", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    assertThrows(NotAuthorizedException.class,
                            () -> this.usersResource.updateCurrentUser("Token not-a-jwt", update("one", null, null, null, null)));
                }),
                Arguments.of("R4.7", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.updateCurrentUser(authToken("neo"), update("one", "one@zion.test", "white-rabbit", "chosen", "one.png"));
                    var stored = this.userRecords.findById("one").orElseThrow();
                    assertEquals("one@zion.test", stored.email());
                    assertEquals("chosen", stored.bio());
                    assertEquals("one.png", stored.image());
                    assertCredentialMaterial(stored.passwordCredential(), "white-rabbit");
                }));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("profileRequirements")
    void viewProfile(String requirement, Executable requirementCheck) {
        assertDoesNotThrow(requirementCheck, requirement);
    }

    Stream<Arguments> profileRequirements() {
        return Stream.of(
                Arguments.of("R5.1", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    var response = this.usersResource.viewProfile(null, "neo");
                    var profile = body(response).getJsonObject("profile");
                    assertEquals("neo", profile.getString("username"));
                    assertFalse(profile.getBoolean("following"));
                }),
                Arguments.of("R5.2", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.registerUser(user("trinity", "trinity@zion.test", "rabbit"));
                    this.usersResource.followUser(authToken("neo"), "trinity");
                    var response = this.usersResource.viewProfile(authToken("neo"), "trinity");
                    assertTrue(body(response).getJsonObject("profile").getBoolean("following"));
                }),
                Arguments.of("R5.3", (Executable) () -> assertThrows(NotFoundException.class,
                        () -> this.usersResource.viewProfile(null, "smith"))),
                Arguments.of("R5.4", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    var response = this.usersResource.viewProfile("Token not-a-jwt", "neo");
                    assertEquals("neo", body(response).getJsonObject("profile").getString("username"));
                    assertFalse(body(response).getJsonObject("profile").getBoolean("following"));
                }));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("followRequirements")
    void followUser(String requirement, Executable requirementCheck) {
        assertDoesNotThrow(requirementCheck, requirement);
    }

    Stream<Arguments> followRequirements() {
        return Stream.of(
                Arguments.of("R6.1", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.registerUser(user("trinity", "trinity@zion.test", "rabbit"));
                    var response = this.usersResource.followUser(authToken("neo"), "trinity");
                    assertTrue(body(response).getJsonObject("profile").getBoolean("following"));
                }),
                Arguments.of("R6.2", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.registerUser(user("trinity", "trinity@zion.test", "rabbit"));
                    this.usersResource.followUser(authToken("neo"), "trinity");
                    var response = this.usersResource.followUser(authToken("neo"), "trinity");
                    assertTrue(body(response).getJsonObject("profile").getBoolean("following"));
                }),
                Arguments.of("R6.3", (Executable) () -> assertThrows(NotAuthorizedException.class,
                        () -> this.usersResource.followUser(null, "trinity"))),
                Arguments.of("R6.4", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.followUser(authToken("neo"), "neo"));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.followUser(authToken("neo"), "smith"));
                }),
                Arguments.of("R6.5", (Executable) () -> assertThrows(NotAuthorizedException.class,
                        () -> this.usersResource.followUser("Token not-a-jwt", "trinity"))),
                Arguments.of("R6.6", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.registerUser(user("trinity", "trinity@zion.test", "rabbit"));
                    this.usersResource.followUser(authToken("neo"), "trinity");
                    assertTrue(this.userRecords.findById("neo").orElseThrow().follows("trinity"));
                }));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("unfollowRequirements")
    void unfollowUser(String requirement, Executable requirementCheck) {
        assertDoesNotThrow(requirementCheck, requirement);
    }

    Stream<Arguments> unfollowRequirements() {
        return Stream.of(
                Arguments.of("R7.1", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.registerUser(user("trinity", "trinity@zion.test", "rabbit"));
                    this.usersResource.followUser(authToken("neo"), "trinity");
                    var response = this.usersResource.unfollowUser(authToken("neo"), "trinity");
                    assertFalse(body(response).getJsonObject("profile").getBoolean("following"));
                }),
                Arguments.of("R7.2", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.registerUser(user("trinity", "trinity@zion.test", "rabbit"));
                    var response = this.usersResource.unfollowUser(authToken("neo"), "trinity");
                    assertFalse(body(response).getJsonObject("profile").getBoolean("following"));
                }),
                Arguments.of("R7.3", (Executable) () -> assertThrows(NotAuthorizedException.class,
                        () -> this.usersResource.unfollowUser(null, "trinity"))),
                Arguments.of("R7.4", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.unfollowUser(authToken("neo"), "neo"));
                    assertThrows(BadRequestException.class,
                            () -> this.usersResource.unfollowUser(authToken("neo"), "smith"));
                }),
                Arguments.of("R7.5", (Executable) () -> assertThrows(NotAuthorizedException.class,
                        () -> this.usersResource.unfollowUser("Token not-a-jwt", "trinity"))),
                Arguments.of("R7.6", (Executable) () -> {
                    this.usersResource.registerUser(user("neo", "neo@zion.test", "rabbit"));
                    this.usersResource.registerUser(user("trinity", "trinity@zion.test", "rabbit"));
                    this.usersResource.followUser(authToken("neo"), "trinity");
                    this.usersResource.unfollowUser(authToken("neo"), "trinity");
                    assertFalse(this.userRecords.findById("neo").orElseThrow().follows("trinity"));
                }));
    }

    private JsonObject user(String username, String email, String password) {
        var user = Json.createObjectBuilder();
        add(user, "username", username);
        add(user, "email", email);
        add(user, "password", password);
        return Json.createObjectBuilder().add("user", user).build();
    }

    private JsonObject login(String email, String password) {
        var user = Json.createObjectBuilder();
        add(user, "email", email);
        add(user, "password", password);
        return Json.createObjectBuilder().add("user", user).build();
    }

    private JsonObject update(String username, String email, String password, String bio, String image) {
        var user = Json.createObjectBuilder();
        add(user, "username", username);
        add(user, "email", email);
        add(user, "password", password);
        add(user, "bio", bio);
        add(user, "image", image);
        return Json.createObjectBuilder().add("user", user).build();
    }

    private JsonObject body(jakarta.ws.rs.core.Response response) {
        var entity = response.getEntity();
        assertNotNull(entity);
        return (JsonObject) entity;
    }

    private void assertCredentialMaterial(PasswordCredential credential, String plaintext) {
        assertNotNull(credential.salt());
        assertNotNull(credential.hash());
        assertFalse(credential.salt().contains(plaintext));
        assertFalse(credential.hash().contains(plaintext));
        assertTrue(credential.matches(plaintext));
    }

    private String authToken(String username) {
        var response = this.usersResource.authenticateUser(login(username + "@zion.test", "rabbit"));
        return "Token " + body(response).getJsonObject("user").getString("token");
    }

    private void assertJwt(String token) {
        assertEquals(3, token.split("\\.", -1).length);
    }

    private void add(jakarta.json.JsonObjectBuilder builder, String name, String value) {
        if (value != null) {
            builder.add(name, value);
        }
    }
}
