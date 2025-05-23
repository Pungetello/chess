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

    @Test
    @DisplayName("The First Test")
    public void AuthClearTest(){
        AuthData data = new AuthData();
        data.setAuthToken("authtoken");
        data.setUsername("test");

        new SQLAuthDAO().createAuth(data);
        new SQLAuthDAO().clear();

        Assertions.assertTrue(//database length == 0);
    }



}