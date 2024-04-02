package ui;

import com.google.gson.Gson;
import model.response.*;
import model.request.*;
import ui.exception.ResponseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

// I need to create another class called websocketfacade and have this class call it to initiate websocket stuff

public class ServerFacade {
    private static String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }
    public static RegisterResponse register(String username, String password, String email) throws ResponseException {
        var request = new RegisterRequest(username, password, email);
        var path = "/user";

        return makeRequest("POST", path, request, null, RegisterResponse.class);
    }
    public static LoginResponse login(String username, String password) throws ResponseException {
        var request = new LoginRequest(username, password);
        var path = "/session";

        return makeRequest("POST", path, request, null, LoginResponse.class);
    }
    public static LogoutResponse logout(String auth) throws ResponseException {
        var path = "/session";

        return makeRequest("DELETE", path, null, auth, LogoutResponse.class);
    }
    public static ListGamesResponse listGames(String auth) throws ResponseException {
        var path = "/game";

        return makeRequest("GET", path, null, auth, ListGamesResponse.class);
    }
    public static GameResponse createGame(String auth, String gameName) throws ResponseException {
        var request = new GameRequest(gameName);
        var path = "/game";

        return makeRequest("POST", path, request, auth, GameResponse.class);
    }
    public static JoinGameResponse joinGame(String auth, String playerColor, String gameID) throws ResponseException {
        var request = new JoinGameRequest(playerColor, gameID);
        var path = "/game";

        return makeRequest("PUT", path, request, auth, JoinGameResponse.class);
    }

    public WebSocketFacade initiateWebSocket(OnMessageReceivedListener listener) throws ResponseException {
        return new WebSocketFacade(serverUrl, listener);
    }
    private static <T> T makeRequest(String method, String path, Object request, String auth, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (auth != null) {
                http.setRequestProperty("Authorization", auth);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private static boolean isSuccessful(int status) {
        return status / 100 == 2;
    }



}
