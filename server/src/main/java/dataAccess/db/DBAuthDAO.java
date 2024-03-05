package dataAccess.db;

import dataAccess.AuthDAO;
import model.AuthData;

import java.util.ArrayList;

public class DBAuthDAO extends DBDAO implements AuthDAO {


    public boolean clear() {
        String sql = "DELETE FROM auth;";

        return clearTable(sql);
    }
    public String createAuth(String authToken, String username){
        var newAuth = new AuthData(authToken, username);
        return authToken;
    }
    public AuthData getAuth(String authToken){
        return null;
    }

    public boolean deleteAuth(String authToken){
        return true;
    }



}
