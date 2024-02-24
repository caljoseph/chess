package dataAccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDAO {

    boolean clear();
    String createAuth(String authToken, String userId);
    AuthData getAuth(String authToken);
    boolean deleteAuth(String authToken) throws DataAccessException;
}
