package dataAccess.db;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;

import java.sql.SQLException;

public class DBDAO {
    static boolean clearTable(String sql) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                if (preparedStatement.executeUpdate() == 1) {
                    return true;
                }
                else return false;
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
