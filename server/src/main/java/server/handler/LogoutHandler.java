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

        var request = new LogoutRequest(req.headers("authorization"));
        var result = UserService.logout(request);

        if (result instanceof FailureResponse){
            setStatus(res, (FailureResponse) result);
        } else { res.status(200); }

        return serializer.toJson(result);
    }
}