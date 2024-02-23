package server.handler;

import com.google.gson.Gson;
import model.FailureResponse;
import model.LoginRequest;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    public static Object handleRequest(Request req, Response res) {
        var serializer = new Gson();
        var request = serializer.fromJson(req.body(), LoginRequest.class);

        var result = UserService.login(request);

        if (result instanceof FailureResponse){
            setStatus(res, (FailureResponse) result);
        } else { res.status(200); }

        return serializer.toJson(result);
    }
}
