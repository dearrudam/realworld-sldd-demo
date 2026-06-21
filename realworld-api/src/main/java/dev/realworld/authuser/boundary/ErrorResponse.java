package dev.realworld.authuser.boundary;
import java.util.List; public record ErrorResponse(Errors errors){ public static ErrorResponse of(String message){return new ErrorResponse(new Errors(List.of(message)));} public record Errors(List<String> body){} }
