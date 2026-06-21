package dev.realworld.authuser.boundary;

import dev.realworld.authuser.control.DuplicateUserException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.Comparator;

public class RealworldExceptionMapper {
    @ServerExceptionMapper
    public Response validation(ConstraintViolationException exception) {
        var messages = exception.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .sorted(Comparator.naturalOrder())
                .toList();
        return Response.status(422).entity(new ErrorsResponse(new Errors(messages))).build();
    }

    @ServerExceptionMapper
    public Response web(WebApplicationException exception) {
        return Response.status(exception.getResponse().getStatus()).entity(ErrorsResponse.of(exception.getMessage())).build();
    }
}
