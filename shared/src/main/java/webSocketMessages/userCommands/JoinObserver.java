package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    int gameID;

    public JoinObserver(String authToken) {
        super(authToken);
        commandType = CommandType.JOIN_OBSERVER;
    }
}
