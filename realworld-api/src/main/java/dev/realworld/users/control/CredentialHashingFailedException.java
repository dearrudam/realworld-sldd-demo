package dev.realworld.users.control;

public class CredentialHashingFailedException extends IllegalStateException {

    public CredentialHashingFailedException(Throwable cause) {
        super("Password credential hashing failed", cause);
    }
}
