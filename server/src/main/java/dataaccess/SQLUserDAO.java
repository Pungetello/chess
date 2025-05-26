package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
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
            CREATE TABLE IF NOT EXISTS user(
              	username VARCHAR(255) NOT NULL,
              	password VARCHAR(255) NOT NULL,
              	email VARCHAR(255) NOT NULL,
              	PRIMARY KEY (username)
              )"""
    };

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE user");
            preparedStatement.executeUpdate();

        } catch (SQLException ex){
            throw new DataAccessException("SQL");
        }
    }

    public void createUser(UserData data) throws DataAccessException{
        throw new DataAccessException("SQL");
    }

    public UserData getUser(String username) throws DataAccessException{
        throw new DataAccessException("SQL");
    }

}
