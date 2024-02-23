package model;

public class LoginResponse implements Response {
    public final String username;
    public final String authToken;

    public LoginResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }
}