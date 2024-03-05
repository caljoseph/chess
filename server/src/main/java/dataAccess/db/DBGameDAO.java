package dataAccess.db;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import model.GameData;

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
        return null;
    }
    public ArrayList<GameData> listGames(){
        return new ArrayList<>();
    }
    public void updateGame(String gameId, GameData updatedGameData){

    }

}
