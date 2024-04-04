package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    Integer gameID;
    public Resign(String authToken, int gameID) {
        super(authToken);
        commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
