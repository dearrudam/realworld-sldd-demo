package dev.realworld.authuser.control;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

class InvalidCredentialsException extends WebApplicationException {
    InvalidCredentialsException() { super("invalid credentials", Response.Status.UNAUTHORIZED); }
}
