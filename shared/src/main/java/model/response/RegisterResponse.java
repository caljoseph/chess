package model.response;

public class RegisterResponse implements Response {
    public final String username;
    public final String authToken;

    public RegisterResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }
}
