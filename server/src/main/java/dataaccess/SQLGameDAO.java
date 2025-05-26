package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;

import java.sql.SQLException;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("SQL");
        }
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
        throw new DataAccessException("SQL");
    }

    public void updateGame(int gameID, GameData data) throws DataAccessException{
        throw new DataAccessException("SQL");
    }
}
