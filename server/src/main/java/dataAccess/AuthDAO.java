package dataAccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDAO {

    boolean clear();
    void createAuth(String authToken, String userId);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken) throws DataAccessException;
}