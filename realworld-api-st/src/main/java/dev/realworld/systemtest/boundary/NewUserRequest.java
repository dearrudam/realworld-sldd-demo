package dev.realworld.systemtest.boundary;

public record NewUserRequest(NewUserDto user) {
    public static NewUserRequest of(String username, String email, String password) {
        return new NewUserRequest(new NewUserDto(username, email, password));
    }

    public record NewUserDto(String username, String email, String password) {}
}