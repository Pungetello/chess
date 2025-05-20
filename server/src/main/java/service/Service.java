package service;
import dataaccess.*;
import model.*;
import requests.*;
import results.*;
import java.util.UUID;

public class Service {
    public void clear(){
        new MemoryAuthDAO().clear();
        new MemoryGameDAO().clear();
        new MemoryUserDAO().clear();
    }

    public RegisterResult register(RegisterRequest request){
        MemoryUserDAO userDataAccess = new MemoryUserDAO();
        //if (userDataAccess.getUser(request.username()) != null){
            //return error somehow?
        //}
        UserData newUser = new UserData();
        newUser.setEmail(request.email());
        newUser.setPassword(request.password());
        newUser.setUsername(request.username());          //should probably create constructor
        userDataAccess.createUser(newUser);

        AuthData newAuth = new AuthData();
        newAuth.setUsername(request.username());
        newAuth.setAuthToken(UUID.randomUUID().toString());
        new MemoryAuthDAO().createAuth(newAuth);

        return new RegisterResult(newAuth.getUsername(), newAuth.getAuthToken());
    }

    //other 6

}
