package dataAccess.memory;

import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private static ArrayList<UserData> memory = new ArrayList<>();
    public boolean clear() {
        memory.clear();
        return true;
    }
    public void createUser(UserData user) {
        memory.add(user);
    }
    public UserData getUser(String username) {
        for (UserData user : memory) {
            if (user.username().equals(username)){
                return user;
            }
        }
        return null;
    }
    public boolean verifyUser(String username, String password) {
        UserData found = getUser(username);
        return found != null && found.password().equals(password);
    }
}
