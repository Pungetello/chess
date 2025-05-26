package dataaccess;

import model.AuthData;
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
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1,data.getUsername());

            //hash password here!

            preparedStatement.setString(2,data.getPassword());
            preparedStatement.setString(3,data.getEmail());
            preparedStatement.executeUpdate();

        } catch (SQLException ex){
            throw new DataAccessException("SQL");
        }
    }

    public UserData getUser(String username) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("SELECT password, email FROM user WHERE username=?")){
            preparedStatement.setString(1, username);
            try (var response = preparedStatement.executeQuery()){
                if(response.next()){
                    String password = response.getString("password");
                    String email = response.getString("email");
                    UserData data = new UserData();
                    data.setUsername(username);
                    data.setPassword(password);
                    data.setEmail(email);
                    return data;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("SQL");
        }
        return null;
    }

}
