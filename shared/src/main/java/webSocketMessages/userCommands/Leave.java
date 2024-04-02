package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    Integer gameID;
    public Leave(String authToken, int gameID) {
        super(authToken);
        commandType = UserGameCommand.CommandType.LEAVE;
        this.gameID = gameID;
    }
}
