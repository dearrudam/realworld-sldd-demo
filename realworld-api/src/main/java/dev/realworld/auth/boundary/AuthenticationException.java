package dev.realworld.auth.boundary;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

public class AuthenticationException extends WebApplicationException {

    public AuthenticationException(Map<String, List<String>> errors, int status) {
        super(Response.status(status)
                .entity(Map.of("errors", errors))
                .build());
    }
}
