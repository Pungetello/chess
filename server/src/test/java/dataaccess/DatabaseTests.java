package dataaccess;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;

import java.util.HashSet;

public class DatabaseTests {

    @BeforeEach
    public void createTables() throws DataAccessException {
        new SQLAuthDAO();
        new SQLGameDAO();
        new SQLUserDAO();
    }

    @AfterEach
    public void clearTables() throws Exception {
        new SQLAuthDAO().clear();
        new SQLGameDAO().clear();
        new SQLUserDAO().clear();
    }

    @Test
    @DisplayName("Auth Clear Test")
    public void AuthClearTest() throws Exception{
        AuthData data = new AuthData();
        data.setAuthToken("authtoken");
        data.setUsername("test");


        new SQLAuthDAO().createAuth(data);
        new SQLAuthDAO().clear();
        int count = getDatabaseLength("auth");
        Assertions.assertEquals(0, count, "Data remaining in table after clear");
    }

    @Test
    @DisplayName("Create Auth Test")
    public void CreateAuthTest() throws Exception{
        AuthData data = new AuthData();
        data.setAuthToken("authtoken");
        data.setUsername("test");

        new SQLAuthDAO().createAuth(data);

        int count = getDatabaseLength("auth");
        Assertions.assertEquals(1, count, "Data not in auth table after being created");
    }

    @Test
    @DisplayName("Get Auth Test")
    public void GetAuthTest() throws Exception{
        AuthData data = new AuthData();
        data.setAuthToken("authtoken");
        data.setUsername("test");

        SQLAuthDAO dao = new SQLAuthDAO();
        dao.createAuth(data);
        AuthData response = dao.getAuth(data.getAuthToken());

        Assertions.assertEquals(data, response, "Did not receive same username and auth");
    }

    @Test
    @DisplayName("Negative Get Auth Test")
    public void GetAuthNotInDatabaseTest() throws Exception{
        AuthData data = new AuthData();
        data.setAuthToken("authtoken");
        data.setUsername("test");

        SQLAuthDAO dao = new SQLAuthDAO();
        dao.createAuth(data);

        Assertions.assertThrows(DataAccessException.class, () -> dao.getAuth("invalidAuthToken"),
                "should get exception for auth that does not exist");
    }

    @Test
    @DisplayName("Delete Auth Test")
    public void DeleteAuthTest() throws Exception{
        AuthData data = new AuthData();
        data.setAuthToken("authtoken");
        data.setUsername("test");

        SQLAuthDAO dao = new SQLAuthDAO();
        dao.createAuth(data);
        dao.deleteAuth("authtoken");

        int count = getDatabaseLength("auth");
        Assertions.assertEquals(0, count, "Data should not be in table after being deleted");
    }

    @Test
    @DisplayName("Negative Delete Auth Test")
    public void DeleteAuthNotInDatabaseTest() throws Exception{
        SQLAuthDAO dao = new SQLAuthDAO();

        Assertions.assertThrows(DataAccessException.class, () -> dao.deleteAuth("invalidAuthToken"),
                "should get exception for auth that does not exist");
    }


    @Test
    @DisplayName("User Clear Test")
    public void UserClearTest() throws Exception{
        UserData data = new UserData();
        data.setEmail("email");
        data.setPassword("password");
        data.setUsername("test");


        new SQLUserDAO().createUser(data);
        new SQLUserDAO().clear();
        int count = getDatabaseLength("user");
        Assertions.assertEquals(0, count, "Data remaining in table after clear");
    }

    @Test
    @DisplayName("Create User Test")
    public void CreateUserTest() throws Exception{
        UserData data = new UserData();
        data.setEmail("email");
        data.setPassword("password");
        data.setUsername("test");

        SQLUserDAO dao = new SQLUserDAO();
        dao.createUser(data);

        int count = getDatabaseLength("user");
        Assertions.assertEquals(1, count, "Data not in user table after being created");
    }

    @Test
    @DisplayName("Get User Test")
    public void GetUserTest() throws Exception{
        UserData data = new UserData();
        data.setEmail("email");
        data.setPassword("password");
        data.setUsername("test");

        SQLUserDAO dao = new SQLUserDAO();
        dao.createUser(data);
        UserData response = dao.getUser(data.getUsername());

        Assertions.assertEquals(data, response, "Did not receive same user data");
    }

    @Test
    @DisplayName("Negative Get User Test")
    public void GetUserNotInDatabaseTest() throws Exception{
        UserData data = new UserData();
        data.setEmail("email");
        data.setPassword("password");
        data.setUsername("test");

        SQLUserDAO dao = new SQLUserDAO();
        dao.createUser(data);
        UserData response = dao.getUser("invalidUsername");

        Assertions.assertNull(response, "should get null for username that does not exist");
    }

    @Test
    @DisplayName("Game Clear Test")
    public void GameClearTest() throws Exception{
        GameData data = new GameData();
        data.setGameID(123);
        data.setGameName("test");
        data.setGame(new ChessGame());

        SQLGameDAO dao = new SQLGameDAO();
        dao.createGame(data);
        dao.clear();

        int count = getDatabaseLength("game");
        Assertions.assertEquals(0, count, "Data remaining in table after clear");
    }

    @Test
    @DisplayName("Create Game Test")
    public void CreateGameTest() throws Exception{
        GameData data = new GameData();
        data.setGameID(123);
        data.setGameName("test");
        data.setGame(new ChessGame());

        SQLGameDAO dao = new SQLGameDAO();
        dao.createGame(data);

        int count = getDatabaseLength("game");
        Assertions.assertEquals(1, count, "Data not in game table after being created");
    }

    @Test
    @DisplayName("Get Game Test")
    public void GetGameTest() throws Exception{
        GameData data = new GameData();
        data.setGameID(123);
        data.setGameName("test");
        data.setGame(new ChessGame());

        SQLGameDAO dao = new SQLGameDAO();
        dao.createGame(data);
        GameData response = dao.getGame(data.getGameID());

        Assertions.assertEquals(data, response, "Did not receive same user data");
    }

    @Test
    @DisplayName("Negative Get Game Test")
    public void GatGameNotInDatabaseTest() throws Exception{
        GameData data = new GameData();
        data.setGameID(123);
        data.setGameName("test");
        data.setGame(new ChessGame());

        SQLGameDAO dao = new SQLGameDAO();
        dao.createGame(data);

        Assertions.assertThrows(DataAccessException.class, () -> dao.getGame(999),
                "should get exception for game that does not exist");
    }

    @Test
    @DisplayName("List Games Test")
    public void ListGamesTest() throws Exception{
        GameData data = new GameData();
        data.setGameID(123);
        data.setGameName("test");
        data.setGame(new ChessGame());

        GameData data2 = new GameData();
        data2.setGameID(999);
        data2.setGameName("test2");
        data2.setGame(new ChessGame());

        SQLGameDAO dao = new SQLGameDAO();
        dao.createGame(data);
        dao.createGame(data2);

        HashSet<GameData> expected = new HashSet<GameData>[data, data2;];
        HashSet<GameData> actual = dao.listGames();

        Assertions.assertEquals(expected, actual, "Should return list of the two games created");

    }

    @Test
    @DisplayName("Negative List Games Test")
    public void NegListGamesTest() throws Exception{        //what's a negative test for this? unauthorized?
        throw new DataAccessException("not implemented");
    }

    @Test
    @DisplayName("Update Game Test")
    public void UpdateGameTest() throws Exception{
        GameData data = new GameData();
        data.setGameID(123);
        data.setGameName("test");
        data.setGame(new ChessGame());

        SQLGameDAO dao = new SQLGameDAO();
        dao.createGame(data);

        GameData newData = new GameData();
        newData.setGameID(123);
        newData.setGameName("alteredName");
        newData.setGame(new ChessGame());

        dao.updateGame(123, newData);

        GameData response = dao.getGame(123);

        Assertions.assertEquals(newData, response, "Did not receive same game data");
    }

    @Test
    @DisplayName("Negative Update Game Test")
    public void UpdateGameNotInDatabaseTest() throws Exception{
        GameData data = new GameData();
        data.setGameID(123);
        data.setGameName("test");
        data.setGame(new ChessGame());

        SQLGameDAO dao = new SQLGameDAO();
        dao.createGame(data);

        Assertions.assertThrows(DataAccessException.class, () -> dao.updateGame(999, data),
                "should get exception for game that does not exist");
    }


    private int getDatabaseLength(String dbName) throws Exception{
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM " + dbName + ";");
             var rs = preparedStatement.executeQuery()) {

            int count = -1;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        }
    }



}