package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
    Integer gameID;
    ChessMove move;

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    public MakeMove(String authToken, ChessMove move, int gameID) {
        super(authToken);
        commandType = CommandType.MAKE_MOVE;
        this.move = move;
        this.gameID = gameID;
    }
}
