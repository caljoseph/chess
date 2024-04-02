package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }
}
