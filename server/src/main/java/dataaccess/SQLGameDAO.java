package dataaccess;

import model.GameData;

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
              	gameID INT NOT NULL AUTO_INCREMENT,
              	gameName VARCHAR(255) NOT NULL,
              	whiteUsername VARCHAR(255),
              	blackUsername VARCHAR(255),
              	game longtext NOT NULL,
              	PRIMARY KEY (gameID)
              )"""
    };



    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE game");
            preparedStatement.executeUpdate();

        } catch (SQLException ex){
            throw new DataAccessException("SQL");
        }
    }
    public void createGame(GameData data) throws DataAccessException{
        throw new DataAccessException("SQL");
    }
    public GameData getGame(int gameID) throws DataAccessException{
        throw new DataAccessException("SQL");
    }
    public Collection<GameData> listGames() throws DataAccessException{
        throw new DataAccessException("SQL");
    }
    public void updateGame(int gameID, GameData data) throws DataAccessException{
        throw new DataAccessException("SQL");
    }
}
