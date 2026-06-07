package dev.realworld.users.control;

public sealed class UserFailures extends RuntimeException permits UserFailures.Validation, UserFailures.Authentication {

    public final String field;

    UserFailures(String field, String message) {
        super(message);
        this.field = field;
    }

    public static final class Validation extends UserFailures {
        public Validation(String field, String message) {
            super(field, message);
        }
    }

    public static final class Authentication extends UserFailures {
        public Authentication() {
            super("email or password", "is invalid");
        }
    }
}
