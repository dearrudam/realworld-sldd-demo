package dev.realworld.users.control;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Invalid authentication token");
    }
}
