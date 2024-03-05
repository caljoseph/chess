package dataAccess.db;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import model.GameData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DBGameDAO extends DBDAO implements GameDAO {

    public boolean clear() {
        String sql = "DELETE FROM game_data;";

        return clearTable(sql);
    }



    public String createGame(GameData gameData) {
        String sql = "insert into game_data (white_username, black_username, game_name, game) values (?, ?, ?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {
                Gson gson = new Gson();

                preparedStatement.setString(1, gameData.whiteUsername());
                preparedStatement.setString(2, gameData.blackUsername());
                preparedStatement.setString(3, gameData.gameName());
                preparedStatement.setString(4, gson.toJson(gameData.game()));

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            return String.valueOf(id);
                        } else {
                            throw new SQLException("Failed to retrieve generated keys.");
                        }
                    }
                } else {
                    throw new SQLException("Creating game failed, no rows affected.");
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData getGame(String gameId){
        String sql = "select * from game_data where game_id = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, gameId);

                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String foundGameID = resultSet.getString("game_id");
                        String foundWhiteUN = resultSet.getString("white_username");
                        String foundBlackUN = resultSet.getString("black_username");
                        String foundGameName = resultSet.getString("game_name");
                        String foundGame = resultSet.getString("game");

                        int intGameID = Integer.parseInt(foundGameID);
                        Gson gson = new Gson();
                        var decodedGame = gson.fromJson(foundGame, ChessGame.class);

                        return new GameData(intGameID, foundWhiteUN, foundBlackUN, foundGameName, decodedGame);
                    } else {
                        return null;
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public ArrayList<GameData> listGames() {
        ArrayList<GameData> gamesList = new ArrayList<>();
        String sql = "select * from game_data";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String foundGameID = resultSet.getString("game_id");
                        String foundWhiteUN = resultSet.getString("white_username");
                        String foundBlackUN = resultSet.getString("black_username");
                        String foundGameName = resultSet.getString("game_name");
                        String foundGame = resultSet.getString("game");

                        int intGameID = Integer.parseInt(foundGameID);
                        Gson gson = new Gson();
                        var decodedGame = gson.fromJson(foundGame, ChessGame.class);

                        gamesList.add(new GameData(intGameID, foundWhiteUN, foundBlackUN, foundGameName, decodedGame));
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }

        return gamesList;
    }
    public void updateGame(String gameId, GameData updatedGameData){

    }

}
