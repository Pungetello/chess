package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requests.*;
import results.*;
import service.Service;
import spark.*;

public class Handler {

    public static Object handleClear(Request req, Response res){
        new Service().clear();
        res.status(200);
        return "";
    }

    public static Object handleRegister(Request req, Response res) throws DataAccessException{

        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
        LoginResult result = new Service().register(request);

        //make sure to handle other cases: 400 Bad Request and 403 Already taken
        res.status(200);
        return new Gson().toJson(result);
    }

    public static Object handleLogin(Request req, Response res) {

        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult result = new Service().login(request);

        //still need error handling for 400 Bad Request and 401 Unauthorized
        res.status(200);
        return new Gson().toJson(result);
    }

    public static Object handleLogout(Request req, Response res) {
        String authToken = req.headers("authorization");
        try {
            new Service().logout(authToken);
            res.status(200);
            return "";
        } catch (DataAccessException ex){
            res.status(401);
            return new Gson().toJson(new results.Error("Error: unauthorized"));
        }
    }

    public static Object handleListGames(Request req, Response res) {
        String authToken = req.headers("authorization");
        try {
            ListGamesResult result = new Service().listGames(authToken);
            res.status(200);
            return new Gson().toJson(result);
        } catch (DataAccessException ex){
            res.status(401);
            return new Gson().toJson(new results.Error("Error: unauthorized"));
        }

    }

    public static Object handleCreateGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        try {
            CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
            CreateGameResult result = new Service().createGame(authToken, request);
            res.status(200);
            return new Gson().toJson(result);
        } catch (DataAccessException ex){ // also need to handle 400 Bad Request
            res.status(401);
            return new Gson().toJson(new results.Error("Error: unauthorized"));
        }

    }

    public static Object handleJoinGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        try {
            JoinGameRequest request = new Gson().fromJson(req.body(), JoinGameRequest.class);
            new Service().joinGame(authToken, request);
            res.status(200);
            return "";
        } catch (DataAccessException ex){
            if (ex.getMessage().equals("Unauthorized")) {
                res.status(401);
                return new Gson().toJson(new results.Error("Error: unauthorized"));
            } else if (ex.getMessage().equals("Already taken")){
                res.status(403);
                return new Gson().toJson(new results.Error("Error: already taken"));
            }
            res.status(400);
            return new Gson().toJson(new results.Error("Error: bad request"));
        }
    }
}
