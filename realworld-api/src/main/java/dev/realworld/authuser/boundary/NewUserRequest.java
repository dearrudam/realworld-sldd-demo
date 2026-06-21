package dev.realworld.authuser.boundary;
import jakarta.validation.constraints.Email; import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Pattern; import jakarta.validation.constraints.Size;
public record NewUserRequest(@NotBlank @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username must be alphanumeric") String username, @NotBlank @Email(message = "email must be well-formed") String email, @NotBlank @Size(min = 8, message = "password must be at least 8 characters") String password) {}
