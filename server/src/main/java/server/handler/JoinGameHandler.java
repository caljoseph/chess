package server.handler;

import com.google.gson.Gson;
import server.response.FailureResponse;
import server.request.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler{
    public static Object handleRequest(Request req, Response res) {
        var serializer = new Gson();
        var authToken = req.headers("Authorization");

        if (!authenticate(authToken)) {
            var result = new FailureResponse("Error: unauthorized");
            setStatus(res, (FailureResponse) result);
            return serializer.toJson(result);
        }

        var request = serializer.fromJson(req.body(), JoinGameRequest.class);
        var result = GameService.join(request, authToken);

        if (result instanceof FailureResponse){
            setStatus(res, (FailureResponse) result);
        } else { res.status(200); }

        return serializer.toJson(result);
    }

}