package server.handler;

import com.google.gson.Gson;
import model.FailureResponse;
import model.RegisterRequest;
import service.RegistrationService;
import service.UtilityService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    public static Object handleRequest(Request req, Response res) {
        var serializer = new Gson();
        var request = serializer.fromJson(req.body(), RegisterRequest.class);
        var result = RegistrationService.register(request);

        if (result instanceof FailureResponse){
            setStatus(res, (FailureResponse) result);
        } else {
            res.status(200);
        }
        return serializer.toJson(result);
    }
}
