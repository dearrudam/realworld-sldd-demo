package dev.realworld.authuser.boundary;
import dev.realworld.authuser.entity.*;import jakarta.validation.ConstraintViolationException;import jakarta.ws.rs.*;import jakarta.ws.rs.core.*;import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
public class ApiExceptionMapper {
 @ServerExceptionMapper public Response validation(ConstraintViolationException e){var msgs=e.getConstraintViolations().stream().map(v->v.getMessage()).toList();return Response.status(422).entity(new ErrorResponse(new ErrorResponse.Errors(msgs))).build();}
 @ServerExceptionMapper public Response duplicate(DuplicateUserException e){return Response.status(422).entity(ErrorResponse.of(e.getMessage())).build();}
 @ServerExceptionMapper public Response invalid(InvalidCredentialsException e){return Response.status(401).entity(ErrorResponse.of(e.getMessage())).build();}
 @ServerExceptionMapper public Response notFound(UserNotFoundException e){return Response.status(404).entity(ErrorResponse.of(e.getMessage())).build();}
 @ServerExceptionMapper public Response badToken(InvalidTokenException e){return Response.status(401).entity(ErrorResponse.of("invalid token")).build();}
}
