package dataAccess.db;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;

import java.sql.SQLException;

public class DBDAO {
    static boolean clearTable(String sql) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
                //I could check here to see if it was truly cleared
                return true;
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
