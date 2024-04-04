package webSocketMessages.userCommands;

import chess.ChessGame;

public class Leave extends UserGameCommand {
    Integer gameID;
    ChessGame.TeamColor playerColor;
    public Leave(String authToken, int gameID, ChessGame.TeamColor color) {
        super(authToken);
        commandType = UserGameCommand.CommandType.LEAVE;
        this.gameID = gameID;
        playerColor = color;
    }

    public Integer getGameID() {
        return gameID;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
