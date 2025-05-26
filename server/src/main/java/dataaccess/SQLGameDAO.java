package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class SQLGameDAO extends SQL implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase(createStatements);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game(
              	gameID INT NOT NULL,
              	gameName VARCHAR(255) NOT NULL,
              	whiteUsername VARCHAR(255),
              	blackUsername VARCHAR(255),
              	game longtext NOT NULL,
              	PRIMARY KEY (gameID)
              )"""
    }; // make the gameID auto-increment?



    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE game");
            preparedStatement.executeUpdate();

        } catch (SQLException ex){
            throw new DataAccessException("SQL");
        }
    }

    public void createGame(GameData data) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("INSERT INTO game (gameID, gameName, game) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1,data.getGameID());
            preparedStatement.setString(2,data.getGameName());

            String gameAsJson = new Gson().toJson(data.getGame());

            preparedStatement.setString(3,gameAsJson);
            preparedStatement.executeUpdate();

        } catch (SQLException ex){
            throw new DataAccessException("SQL");
        }
    }

    public GameData getGame(int gameID) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("SELECT gameName, whiteUsername, blackUsername, game FROM game WHERE gameID=?")){
            preparedStatement.setInt(1, gameID);
            try (var response = preparedStatement.executeQuery()){
                if(response.next()){
                    GameData data = new GameData();
                    data.setGameID(gameID);
                    data.setGameName(response.getString("gameName"));
                    data.setWhiteUsername(response.getString("whiteUsername"));
                    data.setBlackUsername(response.getString("blackUsername"));
                    data.setGame(new Gson().fromJson(response.getString("game"), ChessGame.class));
                    return data;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("SQL");
        }
        throw new DataAccessException("Game does not exist");
    }

    public Collection<GameData> listGames() throws DataAccessException{
        HashSet<GameData> result = new HashSet<GameData>();
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("SELECT gameID, gameName, whiteUsername, blackUsername, game FROM game")){
            try (var response = preparedStatement.executeQuery()){
                while(response.next()){
                    GameData data = new GameData();
                    data.setGameID(response.getInt("gameID"));
                    data.setGameName(response.getString("gameName"));
                    data.setWhiteUsername(response.getString("whiteUsername"));
                    data.setBlackUsername(response.getString("blackUsername"));
                    data.setGame(new Gson().fromJson(response.getString("game"), ChessGame.class));
                    result.add(data);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("SQL");
        }
        return result;
    }

    public void updateGame(int gameID, GameData data) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("UPDATE game SET gameName = ?, whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?")){

            preparedStatement.setString(1, data.getGameName());
            preparedStatement.setString(2, data.getWhiteUsername());
            preparedStatement.setString(3, data.getBlackUsername());
            preparedStatement.setString(4, new Gson().toJson(data.getGame()));
            preparedStatement.setInt(5, gameID);

            int rowsEffected = preparedStatement.executeUpdate();

            if(rowsEffected < 1){
                throw new DataAccessException("Game does not exist");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("SQL");
        }
    }
}
