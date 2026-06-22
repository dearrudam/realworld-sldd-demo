package dev.realworld.auth.boundary;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class RealWorldExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(RealWorldExceptionMapper.class.getName());

    @Override
    public Response toResponse(Exception exception) {
        LOG.log(Level.SEVERE, "Unhandled exception in request", exception);
        // WebApplicationException already has a proper response (e.g., RegistrationException, AuthenticationException)
        if (exception instanceof WebApplicationException wae) {
            return wae.getResponse();
        }
        // Unknown errors → 500
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("errors", Map.of("server", List.of("an unexpected error occurred"))))
                .build();
    }
}
