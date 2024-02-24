package server.handler;

import model.FailureResponse;
import service.UtilityService;
import spark.Request;
import spark.Response;

public class Handler {
    public static Object handleRequest(Request req, Response res){
        return "Base class";
    }
    public static boolean authenticate(String authToken){
        return UtilityService.authenticate(authToken);
    }
    public static void setStatus(Response res, FailureResponse fail){
        String message = fail.message;
        switch (message) {
            case "Error: bad request":
                res.status(400);
                break;
            case "Error: unauthorized":
                res.status(401);
                break;
            case "Error: already taken":
                res.status(403);
                break;
            case "Error: description":
                res.status(500);
                break;
            default:
                res.status(500);
        }
    }

}
