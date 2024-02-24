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

    public boolean deleteAuth(String authToken) throws DataAccessException{
        // check if this authToken exists somewhere
        var auth = getAuth(authToken);
        if (auth == null) { return false; }

        // if it does, extract the username and delete all AuthData objects that match
        deleteUserAuth(auth.userName());
        return true;
    }
    public void deleteUserAuth(String username) {
        Iterator<AuthData> iterator = memory.iterator();
        while (iterator.hasNext()) {
            AuthData auth = iterator.next();
            if (auth.userName().equals(username)) {
                iterator.remove();
            }
        }
    }

}
