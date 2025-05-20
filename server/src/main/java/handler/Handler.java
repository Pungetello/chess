package handler;

import service.Service;
import spark.*;

public class Handler {

    public static Object handleClear(Request req, Response res){
        Service.clear();
        res.status(200);
        return "";
    }

    //other 6
}
