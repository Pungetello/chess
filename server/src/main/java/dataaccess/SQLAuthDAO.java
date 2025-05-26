package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {


    public SQLAuthDAO() throws DataAccessException {
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
            CREATE TABLE IF NOT EXISTS auth(
             	username VARCHAR(255) NOT NULL,
             	authToken VARCHAR(255) NOT NULL,
             	PRIMARY KEY (authToken)
             )"""
    }; // mark username as foreign key?

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth");
            preparedStatement.executeUpdate();

        } catch (SQLException ex){
            throw new DataAccessException("SQL");
        }
    }

    public void createAuth(AuthData data) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (username, authToken) VALUES (?, ?)")) {
                preparedStatement.setString(1,data.getUsername());
                preparedStatement.setString(2,data.getAuthToken());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex){
            throw new DataAccessException("SQL");
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        throw new DataAccessException("SQL");
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        throw new DataAccessException("SQL");
    }
}
