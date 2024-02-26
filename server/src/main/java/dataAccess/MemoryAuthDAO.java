package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Iterator;

public class MemoryAuthDAO implements AuthDAO{
    private static ArrayList<AuthData> memory = new ArrayList<>();

    public boolean clear() {
        memory.clear();
        return true;
    }
    public String createAuth(String authToken, String username){
        var newAuth = new AuthData(authToken, username);
        memory.add(newAuth);
        return authToken;
    }
    public AuthData getAuth(String authToken){
        for (AuthData auth : memory) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    public boolean deleteAuth(String authToken){
        // check if this authToken exists somewhere
        var auth = getAuth(authToken);
        if (auth == null) { return false; }

        // only remove that single authToken!!!! For some reason????
        memory.remove(auth);
        return true;
    }

}
