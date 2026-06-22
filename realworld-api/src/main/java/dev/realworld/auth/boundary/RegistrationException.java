package dev.realworld.auth.boundary;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

public class RegistrationException extends WebApplicationException {

    private final Map<String, List<String>> errors;

    public RegistrationException(Map<String, List<String>> errors, int status) {
        super(Response.status(status)
                .entity(Map.of("errors", errors))
                .build());
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
