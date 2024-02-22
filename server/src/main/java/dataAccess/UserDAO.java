package dataAccess;

import model.UserData;

public interface UserDAO {

    boolean clear();
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username);
}
