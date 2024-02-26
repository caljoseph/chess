package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MemoryGameDAO implements GameDAO {
    public static ArrayList<GameData> memory = new ArrayList<>();
    public boolean clear() {
        memory.clear();
        return true;
    }
    public void createGame(GameData gameData){
        memory.add(gameData);
    }
    public GameData getGame(String gameId){
        for (GameData game : memory) {
            if (String.valueOf(game.gameID()).equals(gameId)) {
                return game;
            }
        }
        return null;
    }
    public ArrayList<GameData> listGames(){
        return memory;
    }
    public void updateGame(String gameId, GameData updatedGameData){
        Iterator<GameData> iterator = memory.iterator();

        while (iterator.hasNext()) {
            GameData game = iterator.next();
            if (String.valueOf(game.gameID()).equals(gameId)) {
                iterator.remove();
                memory.add(updatedGameData);
                break;
            }
        }
    }
}
