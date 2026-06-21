package dev.realworld.authuser.boundary;

import dev.realworld.authuser.entity.DuplicateUserException;
import dev.realworld.authuser.entity.InvalidCredentialsException;
import dev.realworld.authuser.entity.UserNotFoundException;
import io.quarkus.hibernate.validator.runtime.jaxrs.ResteasyReactiveViolationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

class AuthUserExceptionMapper {
    @ServerExceptionMapper
    Response constraintViolation(ResteasyReactiveViolationException exception) {
        var messages = exception.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .toList();
        return response(422, new ErrorResponse(new ErrorResponse.Errors(messages)));
    }

    @ServerExceptionMapper
    Response duplicateUser(DuplicateUserException exception) {
        return response(422, ErrorResponse.of(exception.getMessage()));
    }

    @ServerExceptionMapper
    Response invalidCredentials(InvalidCredentialsException exception) {
        return response(401, ErrorResponse.of(exception.getMessage()));
    }

    @ServerExceptionMapper
    Response userNotFound(UserNotFoundException exception) {
        return response(404, ErrorResponse.of(exception.getMessage()));
    }

    Response response(int status, ErrorResponse error) {
        return Response.status(status).entity(error).build();
    }
}
