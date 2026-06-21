package dev.realworld.authuser.control;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

class CurrentUserNotFoundException extends WebApplicationException {
    CurrentUserNotFoundException() { super("current user not found", Response.Status.NOT_FOUND); }
}
