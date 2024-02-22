package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;


public class MemoryGameDAO implements GameDAO {
    private static ArrayList<GameData> memory = new ArrayList<>();
    public boolean clear() {
        memory.clear();
        return true;
    }
    public void createGame(String gameId, String gameData){

    }
    public GameData getGame(String gameId){
        //TODO implement
        return new GameData(1, null, null, null, null);
    }
    public ArrayList<GameData> listGames(){
        return new ArrayList<GameData>();
    }
    public void updateGame(String gameId, GameData updatedGameData){

    }
}
