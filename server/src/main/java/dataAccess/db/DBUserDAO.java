package dataAccess.db;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;
import dataAccess.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;

public class DBUserDAO extends DBDAO implements UserDAO {

    public boolean clear() {
        String sql = "DELETE FROM users;";

        return clearTable(sql);
    }
    public void createUser(UserData user) {
        String sql = "insert into users (username, password, email) values (?, ?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public UserData getUser(String username) {
        String sql = "select * from users where username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);

                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String foundUsername = resultSet.getString("username");
                        String foundPassword = resultSet.getString("password");
                        String foundEmail = resultSet.getString("email");

                        return new UserData(foundUsername, foundPassword, foundEmail);
                    } else {
                        return null;
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean verifyUser(String username, String password) {
        var user = getUser(username);
        if (user == null) {
            return false;
        }
        if (password.equals(user.password())) {
            return true;
        } else {
            return false;
        }
    }

}
