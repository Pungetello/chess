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
        RegisterResult result = new Service().register(request);

        //make sure to handle other cases, this is only success case
        res.status(200);
        return new Gson().toJson(result);
    }

    //other 5
}
