package server.handler;

import com.google.gson.Gson;
import model.response.FailureResponse;
import model.request.RegisterRequest;
import service.RegistrationService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    public static Object handleRequest(Request req, Response res) {
        var serializer = new Gson();
        var request = serializer.fromJson(req.body(), RegisterRequest.class);
        var result = RegistrationService.register(request);

        if (result instanceof FailureResponse){
            setStatus(res, (FailureResponse) result);
        } else { res.status(200); }

        return serializer.toJson(result);
    }
}
