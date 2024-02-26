package server.response;

public class FailureResponse implements Response {
    public final String message;

    public FailureResponse(String error) {
        this.message = error;
    }

    public String getMessage() {
        return message;
    }
}
