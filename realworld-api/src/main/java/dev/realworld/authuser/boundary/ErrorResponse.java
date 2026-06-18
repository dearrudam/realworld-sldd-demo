package dev.realworld.authuser.boundary;

import java.util.List;

public record ErrorResponse(Errors errors) {
    public static ErrorResponse of(List<String> bodyMessages) {
        return new ErrorResponse(new Errors(bodyMessages));
    }

    public record Errors(List<String> body) {}
}