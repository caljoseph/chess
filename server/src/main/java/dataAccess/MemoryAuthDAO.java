package dataAccess;

import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO{
    private static ArrayList<AuthData> memory = new ArrayList<>();
    public boolean clear() {
        memory.clear();
        return true;
    }
    public void createAuth(String authToken, String userId){
        //TODO implement
    }
    public AuthData getAuth(String authToken){
        //TODO implement
        return null;
    }
    public void deleteAuth(String authToken) throws DataAccessException{
        //TODO implement
    }
}
