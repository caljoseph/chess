package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private static ArrayList<UserData> memory = new ArrayList<>();
    public boolean clear() {
        memory.clear();
        return true;
    }
    public void createUser(UserData user) throws DataAccessException {
        //TODO Implement
    }
    public UserData getUser(String username) {
        //TODO Implement
        return new UserData(null,null,null);
    }
}
