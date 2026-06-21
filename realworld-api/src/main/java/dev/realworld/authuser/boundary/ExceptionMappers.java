package dev.realworld.authuser.boundary;

import dev.realworld.authuser.entity.DuplicateUserException;
import dev.realworld.authuser.entity.InvalidCredentialsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.Map;

public final class ExceptionMappers {

    private ExceptionMappers() {
    }

    static Response errorResponse(int status, String message) {
        return Response.status(status)
                .entity(Map.of("errors", Map.of("body", List.of(message))))
                .build();
    }

    @Provider
    public static final class ConstraintViolationMapper
            implements ExceptionMapper<jakarta.validation.ConstraintViolationException> {

        @Override
        public Response toResponse(jakarta.validation.ConstraintViolationException exception) {
            var messages = exception.getConstraintViolations().stream()
                    .map(jakarta.validation.ConstraintViolation::getMessage)
                    .toList();
            return Response.status(422)
                    .entity(Map.of("errors", Map.of("body", messages)))
                    .build();
        }
    }

    @Provider
    public static final class DuplicateUserMapper implements ExceptionMapper<DuplicateUserException> {

        @Override
        public Response toResponse(DuplicateUserException exception) {
            return errorResponse(422, exception.getMessage());
        }
    }

    @Provider
    public static final class InvalidCredentialsMapper implements ExceptionMapper<InvalidCredentialsException> {

        @Override
        public Response toResponse(InvalidCredentialsException exception) {
            return errorResponse(401, exception.getMessage());
        }
    }
}
