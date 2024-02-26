package service;

import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.*;
import server.Server;

public class GameService {
    private static int numGames = 1;
    private static MemoryGameDAO gameDAO = Server.getGameDAO();
    private static MemoryAuthDAO authDAO = Server.getAuthDAO();

    public static Response create(GameRequest request) {
        if (!isValid(request)) {
            return new FailureResponse("Error: bad request");
        }
        // gives it gameID equivalent to the total number of games added
        var newGame = new GameData(numGames, null,null, request.gameName(), null);
        numGames++;
        gameDAO.createGame(newGame);
        return new GameResponse(String.valueOf(newGame.gameID()));
    }
    public static Response listGames() {
        return new ListGamesResponse(gameDAO.listGames());
    }

    private static boolean isValid(GameRequest request){
        return request.gameName() != null;
    }
    private static boolean isValid(JoinGameRequest request){
        return request.gameID() != null;
    }


    public static Response join(JoinGameRequest request, String authToken) {
        // Check object
        if (!isValid(request)) {
            return new FailureResponse("Error: bad request");
        }

        var game = gameDAO.getGame(request.gameID());
        var authTokenInfo = authDAO.getAuth(authToken);
        var username = authTokenInfo.userName();

        // Check fields
        if (game == null || authTokenInfo == null || username == null) {
            return new FailureResponse("Error: bad request");
        }

        // Watcher
        if (request.playerColor() == null) {
            var newGameData = new GameData(game, game.whiteUsername(), game.blackUsername());
            gameDAO.updateGame(request.gameID(), newGameData);
            return new JoinGameResponse();
        }

        // Spot taken
        if ((request.playerColor().equals("BLACK") && game.blackUsername() != null) ||
                (request.playerColor().equals("WHITE") && game.whiteUsername() != null)) {
            return new FailureResponse("Error: already taken");
        }

        // Successful join
        var newGameData = new GameData(game,
                "WHITE".equals(request.playerColor()) ? username : game.whiteUsername(),
                "BLACK".equals(request.playerColor()) ? username : game.blackUsername());

        gameDAO.updateGame(request.gameID(), newGameData);
        return new JoinGameResponse();
    }
}
