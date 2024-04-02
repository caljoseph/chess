package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    Integer gameID;
    public Resign(String authToken) {
        super(authToken);
        commandType = CommandType.JOIN_OBSERVER;
    }
}
