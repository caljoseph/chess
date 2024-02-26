package server.handler;


import com.google.gson.Gson;
import model.response.FailureResponse;
import service.UtilityService;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler {
    public static Object handleRequest(Request req, Response res) {

        if (UtilityService.clear()) {
            res.status(200);
            return "{}";
        } else {
            res.status(500);
            var serializer = new Gson();
            var fail = new FailureResponse( "Error: description");
            return serializer.toJson(fail);
        }
    }
}
