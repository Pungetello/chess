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

    public LoginResult register(RegisterRequest request){
        MemoryUserDAO userDataAccess = new MemoryUserDAO();
        //if (userDataAccess.getUser(request.username()) != null){
            //return error somehow?
        //}
        UserData newUser = new UserData();
        newUser.setEmail(request.email());
        newUser.setPassword(request.password());
        newUser.setUsername(request.username());          //should probably create constructors
        userDataAccess.createUser(newUser);

        AuthData newAuth = new AuthData();
        newAuth.setUsername(request.username());
        newAuth.setAuthToken(UUID.randomUUID().toString());
        new MemoryAuthDAO().createAuth(newAuth);

        return new LoginResult(newAuth.getUsername(), newAuth.getAuthToken());
    }

    public LoginResult login(LoginRequest request){
        MemoryUserDAO userDataAccess = new MemoryUserDAO();
        UserData user = userDataAccess.getUser(request.username());
        //if (user == null || !user.getPassword().equals(request.password())){
            //return error somehow, wrong username or password
        //}
        AuthData newAuth = new AuthData();
        newAuth.setUsername(request.username());
        newAuth.setAuthToken(UUID.randomUUID().toString());
        new MemoryAuthDAO().createAuth(newAuth);

        return new LoginResult(newAuth.getUsername(), newAuth.getAuthToken());
    }

    public void logout(String authToken) throws DataAccessException{
        new MemoryAuthDAO().deleteAuth(authToken);
    }

    //public ListGamesResult listGames(String authToken){}

    //public void createGame(String authToken){}

    //public void joinGame(JoinGameRequest request){}

}
