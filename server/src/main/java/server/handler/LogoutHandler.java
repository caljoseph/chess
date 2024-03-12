package server.handler;

import com.google.gson.Gson;
import model.response.FailureResponse;
import model.request.LogoutRequest;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    public static Object handleRequest(Request req, Response res) {
        var serializer = new Gson();

        var authToken = req.headers("Authorization");

        if (!authenticate(authToken)) {
            var result = new FailureResponse("Error: unauthorized");
            setStatus(res, (FailureResponse) result);
            return serializer.toJson(result);
        }
        var result = UserService.logout(authToken);

        if (result instanceof FailureResponse){
            setStatus(res, (FailureResponse) result);
        } else { res.status(200); }

        return serializer.toJson(result);
    }
}
