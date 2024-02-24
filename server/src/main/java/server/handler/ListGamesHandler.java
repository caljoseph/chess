package server.handler;

import com.google.gson.Gson;
import model.FailureResponse;
import model.GameRequest;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler{
    public static Object handleRequest(Request req, Response res) {
        var serializer = new Gson();
        var authToken = req.headers("Authorization");

        if (!authenticate(authToken)) {
            var result = new FailureResponse("Error: unauthorized");
            setStatus(res, (FailureResponse) result);
            return serializer.toJson(result);
        }

        var result = GameService.listGames();

        if (result instanceof FailureResponse){
            setStatus(res, (FailureResponse) result);
        } else { res.status(200); }

        return serializer.toJson(result);
    }

}