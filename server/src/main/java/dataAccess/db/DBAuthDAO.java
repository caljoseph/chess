package dataAccess.db;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;

public class DBAuthDAO extends DBDAO implements AuthDAO {


    public boolean clear() {
        String sql = "DELETE FROM auth;";

        return clearTable(sql);
    }
    public String createAuth(String authToken, String username){
        if (getAuth(authToken) != null) {
            return null;
        }
        String sql = "insert into auth (authToken, username) values (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
                return authToken;
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public AuthData getAuth(String authToken){

        String sql = "select * from auth where authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);

                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String foundUsername = resultSet.getString("username");
                        return new AuthData(authToken, foundUsername);

                    } else {
                        return null;

                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteAuth(String authToken){
        if (getAuth(authToken) == null) {
            return false;
        }
        String deleteSql = "DELETE FROM auth WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var deleteStatement = conn.prepareStatement(deleteSql)) {
                deleteStatement.setString(1, authToken);

                int rowsAffected = deleteStatement.executeUpdate();

                // Check if the deletion was successful
                if (rowsAffected > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
