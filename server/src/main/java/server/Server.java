package server;

import dataaccess.*;
import handler.*;
import spark.*;
import java.sql.SQLException;
import static dataaccess.DatabaseManager.createDatabase;

public class Server {

    public int run(int desiredPort) {

        try{
            createDatabase();
            new SQLAuthDAO().configureDatabase();
            new SQLGameDAO().configureDatabase();
            new SQLUserDAO().configureDatabase();
        }catch (SQLException ex){
            //idk
        }

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes(); // make into try-catch blocks for exception handling?

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void createRoutes(){
        Spark.delete("/db", Handler::handleClear);
        Spark.post("/user", Handler::handleRegister);
        Spark.post("/session", Handler::handleLogin);
        Spark.delete("/session", Handler::handleLogout);
        Spark.get("/game", Handler::handleListGames);
        Spark.post("/game", Handler::handleCreateGame);
        Spark.put("/game", Handler::handleJoinGame);
    }
}
