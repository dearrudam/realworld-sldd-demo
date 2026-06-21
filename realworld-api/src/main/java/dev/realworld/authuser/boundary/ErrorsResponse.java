package dev.realworld.authuser.boundary;

public record ErrorsResponse(Errors errors) {
    public static ErrorsResponse of(String message) { return new ErrorsResponse(new Errors(java.util.List.of(message))); }
}
