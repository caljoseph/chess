package dataAccess;

import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO{
    private static ArrayList<AuthData> memory = new ArrayList<>();
    public boolean clear() {
        memory.clear();
        return true;
    }
    public void createAuth(String authToken, String username){
        var newAuth = new AuthData(authToken, username);
        memory.add(newAuth);
    }
    public AuthData getAuth(String authToken){
        //TODO implement
        return null;
    }
    public void deleteAuth(String authToken) throws DataAccessException{
        //TODO implement
        // I need to find a way to delete all authTokens associated with this user on logout
    }
}
