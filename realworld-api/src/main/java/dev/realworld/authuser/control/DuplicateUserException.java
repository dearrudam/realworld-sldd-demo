package dev.realworld.authuser.control;

import jakarta.ws.rs.WebApplicationException;

public class DuplicateUserException extends WebApplicationException {
    public DuplicateUserException(String message) { super(message, 422); }
}
