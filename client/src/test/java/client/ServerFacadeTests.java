package client;

import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import requests.*;
import results.*;
import server.Server;
import ui.ServerFacade;

import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

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
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Register test")
    void registerTest() throws Exception {
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        LoginResult result = facade.register(request);
        Assertions.assertTrue(result.authToken().length() > 10);
    }

    @Test
    @DisplayName("Register negative test")
    void registerNegTest() throws Exception {
        RegisterRequest request = new RegisterRequest("name", "password", null);
        Assertions.assertThrows(Exception.class, () -> facade.register(request),
                "should get exception for no email provided");
    }

    @Test
    @DisplayName("Clear Test")
    public void clearTest() throws Exception{
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        facade.register(request);
        facade.clearDatabase();

        int count = getDatabaseLength("auth") + getDatabaseLength("user") + getDatabaseLength("game");
        Assertions.assertEquals(0, count, "Data remaining in table after clear");
    }

    @Test
    @DisplayName("Login test")
    void loginTest() throws Exception {
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        facade.register(request);
        LoginRequest loginRequest = new LoginRequest("name", "password");
        LoginResult result = facade.login(loginRequest);
        Assertions.assertTrue(result.authToken().length() > 10);
    }

    @Test
    @DisplayName("Login negative test")
    void loginNegTest() throws Exception {
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        facade.register(request);
        LoginRequest loginRequest = new LoginRequest("name", "wrong");

        try{
            facade.login(loginRequest);
        } catch (ResponseException e) {
            Assertions.assertEquals(401, e.StatusCode());
        }
        Assertions.assertThrows(Exception.class, () -> facade.login(loginRequest), "should get exception for wrong password");
    }

    @Test
    @DisplayName("Logout test")
    void logoutTest() throws Exception {
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        LoginResult result = facade.register(request);
        facade.logout(result.authToken());
    }

    @Test
    @DisplayName("List Games Test")
    public void listGamesTest() throws Exception{
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        LoginResult result = facade.register(request);

        CreateGameRequest gameRequest = new CreateGameRequest("firstGame");
        facade.createGame(result.authToken(), gameRequest);

        ListGamesResult actual = facade.listGames(result.authToken());
        int count = actual.games().size();
        Assertions.assertEquals(1, count, "Should have one game in list");
    }

    @Test
    @DisplayName("Create Game Test")
    public void createGameTest() throws Exception{
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        LoginResult result = facade.register(request);

        CreateGameRequest gameRequest = new CreateGameRequest("firstGame");
        facade.createGame(result.authToken(), gameRequest);
    }

    @Test
    @DisplayName("Join Game Test")
    public void joinGameTest() throws Exception{
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        LoginResult result = facade.register(request);

        CreateGameRequest gameRequest = new CreateGameRequest("firstGame");
        CreateGameResult gameResult = facade.createGame(result.authToken(), gameRequest);

        facade.joinGame(result.authToken(), new JoinGameRequest("WHITE", gameResult.gameID()));
    }

// still need neg tests for join, create, list, clear, and logout.







    private int getDatabaseLength(String dbName) throws Exception{
        try (var conn = DatabaseManager.getConnection(); // MAKE PRIVATE AGAIN ONCE DONE TESTING
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
