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

        //make sure to handle other cases, this is only success case
        res.status(200);
        return new Gson().toJson(result);
    }

    public static Object handleLogin(Request req, Response res) throws DataAccessException{

        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult result = new Service().login(request);

        //still need error handling
        res.status(200);
        return new Gson().toJson(result);
    }

    //public static Object handleLogout(Request req, Response res) throws DataAccessException{
        //String authToken = new Gson().fromJson(req.headers());
    //}

    //public static Object handleListGames(Request req, Response res) throws DataAccessException{}

    //public static Object handleCreateGame(Request req, Response res) throws DataAccessException{}

    //public static Object handleJoinGame(Request req, Response res) throws DataAccessException{}
}
