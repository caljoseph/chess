package dataAccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;
    private static final String sql_auth = """
        CREATE TABLE IF NOT EXISTS `auth` (
          `authToken` varchar(255) NOT NULL,
          `username` varchar(255) NOT NULL,
          PRIMARY KEY (`authToken`),
          KEY `userName` (`username`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """;
    private static final String sql_users = """
        CREATE TABLE IF NOT EXISTS `users` (
          `username` varchar(255) NOT NULL,
          `password` varchar(255) NOT NULL,
          `email` varchar(255) NOT NULL,
          PRIMARY KEY (`username`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """;
    private static final String sql_game_data = """
        CREATE TABLE IF NOT EXISTS `game_data` (
          `game_id` bigint unsigned NOT NULL AUTO_INCREMENT,
          `white_username` varchar(255) DEFAULT NULL,
          `black_username` varchar(255) DEFAULT NULL,
          `game_name` varchar(255) NOT NULL,
          `game` varchar(2000) NOT NULL,
          PRIMARY KEY (`game_id`),
          UNIQUE KEY `game_id` (`game_id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            createTables();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public static void createTables() throws DataAccessException {
        createTable(sql_auth);
        createTable(sql_users);
        createTable(sql_game_data);
    }

    private static void createTable(String sql) throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}