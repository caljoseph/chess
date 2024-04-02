package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    Integer gameID;
    public Leave(String authToken) {
        super(authToken);
        commandType = UserGameCommand.CommandType.JOIN_OBSERVER;
    }
}
