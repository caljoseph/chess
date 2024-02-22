package server.handler;


import com.google.gson.Gson;
import model.FailureResponse;
import service.UtilityService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    public static Object handleRequest(Request req, Response res) {

        if (UtilityService.clear()) {
            res.status(200);
            return "{}";
        } else {
            res.status(500);
            var serializer = new Gson();
            var fail = new FailureResponse("message", "Error: description");
            return serializer.toJson(fail);
        }
    }
}
