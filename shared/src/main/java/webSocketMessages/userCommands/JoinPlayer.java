package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    int gameID;
    ChessGame.TeamColor playerColor;
    public JoinPlayer(String authToken) {
        super(authToken);
        commandType = CommandType.JOIN_OBSERVER;
    }
}
