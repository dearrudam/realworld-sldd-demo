package dev.realworld.users.boundary;

import dev.realworld.users.control.AccountOperations;
import dev.realworld.users.control.InvalidTokenException;
import dev.realworld.users.control.UserFailures;
import dev.realworld.users.control.UserSession;
import dev.realworld.users.entity.UserAccount;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.List;
import java.util.Map;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject AccountOperations accounts;

    @POST
    @Path("/users")
    public Response register(@Valid UserRegistrationEnvelope envelope) {
        var session = accounts.register(envelope.user.email, envelope.user.username, envelope.user.password);
        return Response.status(Response.Status.CREATED).entity(UserEnvelope.from(session)).build();
    }

    @POST
    @Path("/users/login")
    public UserEnvelope login(@Valid UserLoginEnvelope envelope) {
        return UserEnvelope.from(accounts.login(envelope.user.email, envelope.user.password));
    }

    @GET
    @Path("/user")
    public UserEnvelope current(@HeaderParam("Authorization") String authorization) {
        return UserEnvelope.from(accounts.current(tokenFrom(authorization)));
    }

    @PUT
    @Path("/user")
    public UserEnvelope update(@HeaderParam("Authorization") String authorization, @Valid UserUpdateEnvelope envelope) {
        var user = envelope.user == null ? new UserUpdate() : envelope.user;
        return UserEnvelope.from(accounts.update(tokenFrom(authorization), user.email, user.username, user.password, user.bio, user.image));
    }

    String tokenFrom(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new InvalidTokenException();
        }
        return authorization.substring("Bearer ".length());
    }

    @ServerExceptionMapper
    Response mapValidation(ConstraintViolationException exception) {
        return Response.status(422).entity(ErrorsEnvelope.of("validation", "is invalid")).build();
    }

    @ServerExceptionMapper
    Response mapUserFailure(UserFailures.Validation exception) {
        return Response.status(422).entity(ErrorsEnvelope.of(exception.field, exception.getMessage())).build();
    }

    @ServerExceptionMapper
    Response mapAuthentication(UserFailures.Authentication exception) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(ErrorsEnvelope.of(exception.field, exception.getMessage())).build();
    }

    @ServerExceptionMapper
    Response mapToken(InvalidTokenException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(ErrorsEnvelope.of("token", "is invalid")).build();
    }

    public static class UserRegistrationEnvelope {
        @NotNull @Valid public UserRegistration user;
    }

    public static class UserRegistration {
        @NotBlank @Email public String email;
        @NotBlank public String username;
        @NotBlank @Size(min = 5) public String password;
    }

    public static class UserLoginEnvelope {
        @NotNull @Valid public UserLogin user;
    }

    public static class UserLogin {
        @NotBlank @Email public String email;
        @NotBlank public String password;
    }

    public static class UserUpdateEnvelope {
        @Valid public UserUpdate user;
    }

    public static class UserUpdate {
        @Email public String email;
        public String username;
        @Size(min = 5) public String password;
        public String bio;
        public String image;
    }

    public record UserEnvelope(UserRepresentation user) {
        static UserEnvelope from(UserSession session) {
            return new UserEnvelope(UserRepresentation.from(session.account(), session.token()));
        }
    }

    public record UserRepresentation(String email, String token, String username, String bio, String image) {
        static UserRepresentation from(UserAccount account, String token) {
            return new UserRepresentation(account.email, token, account.username, account.bio, account.image);
        }
    }

    public record ErrorsEnvelope(Map<String, List<String>> errors) {
        static ErrorsEnvelope of(String field, String message) {
            return new ErrorsEnvelope(Map.of(field, List.of(message)));
        }
    }
}
