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
        LoginResult result = facade.register(request);
        facade.clearDatabase();

        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(result.authToken(), new CreateGameRequest("name")),
                "Data remaining in tables after clear");
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
            Assertions.assertEquals(401, e.statusCode());
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
    @DisplayName("Logout Negative test")
    void logoutNegTest() throws Exception {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout("nonexistentAuthToken"),
                "can't log out if you're not logged in");
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
    @DisplayName("List Games Negative Test")
    public void listGamesNegTest() throws Exception{
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        LoginResult result = facade.register(request);

        CreateGameRequest gameRequest = new CreateGameRequest("firstGame");
        facade.createGame(result.authToken(), gameRequest);

        Assertions.assertThrows(ResponseException.class, () -> facade.listGames("badAuthToken"),
                "Must be logged in to list games");
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
    @DisplayName("Create Game Negative Test")
    public void createGameNegTest() throws Exception{
        CreateGameRequest gameRequest = new CreateGameRequest("firstGame");
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame("badAuthToken", gameRequest),
                "Need to be logged in to make a game");
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

    @Test
    @DisplayName("Join Game Negative Test")
    public void joinGameNegTest() throws Exception{
        RegisterRequest request = new RegisterRequest("name", "password", "email");
        LoginResult result = facade.register(request);

        CreateGameRequest gameRequest = new CreateGameRequest("firstGame");
        CreateGameResult gameResult = facade.createGame(result.authToken(), gameRequest);

        facade.joinGame(result.authToken(), new JoinGameRequest("WHITE", gameResult.gameID()));
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(result.authToken(), new JoinGameRequest("WHITE", gameResult.gameID())),
                "should throw exception for joining game that's already occupied");
    }
}