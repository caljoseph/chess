package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    //this game could be something else, that might be easier
    ChessGame game;
    public LoadGame(ServerMessageType type) {
        super(type);
    }
}
