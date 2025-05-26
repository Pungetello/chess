package dataaccess;

import model.*;
import org.junit.jupiter.api.*;

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
    @DisplayName("Game Clear Test")
    public void GameClearTest() throws Exception{
        AuthData data = new AuthData();
        data.setAuthToken("authtoken"); // NOT YET IMPLEMENTED
        data.setUsername("test");


        new SQLAuthDAO().createAuth(data);
        new SQLAuthDAO().clear();
        int count = getDatabaseLength("game");
        Assertions.assertEquals(0, count, "Data remaining in table after clear");
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