package ui;

import model.response.*;
import model.request.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResponse register(String username, String password, String email) {
        return null;
    }
    public LoginResponse login(String username, String password) {
        return null;
    }
    public LogoutResponse logout(String auth) {
        return null;
    }
    public ListGamesResponse listGames(String auth) {
        return null;
    }
    public GameResponse createGame(String auth, String gameName) {
        return null;
    }
    public JoinGameResponse joinGame(String auth, String playerColor, String gameID) {
        return null;
    }








}
