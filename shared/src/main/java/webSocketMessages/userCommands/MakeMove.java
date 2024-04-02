package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
    Integer gameID;
    ChessMove move;
    public MakeMove(String authToken) {
        super(authToken);
        commandType = CommandType.JOIN_OBSERVER;
    }
}
