package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    boolean clear();
    void createGame(GameData gameData);
    GameData getGame(String gameId);
    ArrayList<GameData> listGames();
    void updateGame(String gameId, GameData updatedGameData);
}
