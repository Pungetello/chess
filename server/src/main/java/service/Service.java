package service;
import dataaccess.*;
import model.*;
import requests.*;
import results.*;

import java.util.*;

public class Service {
    public void clear(){
        new MemoryAuthDAO().clear();
        new MemoryGameDAO().clear();
        new MemoryUserDAO().clear();
    }

    public LoginResult register(RegisterRequest request) throws DataAccessException{
        if (request.username() == null || request.email() == null || request.password() == null){
            throw new DataAccessException("bad request"); //400
        }
        MemoryUserDAO userDataAccess = new MemoryUserDAO();
        if (userDataAccess.getUser(request.username()) != null){
            throw new DataAccessException ("Already taken"); //403
        }
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

    public LoginResult login(LoginRequest request) throws DataAccessException{
        MemoryUserDAO userDataAccess = new MemoryUserDAO();
        UserData user = userDataAccess.getUser(request.username());
        if (user == null){
            throw new DataAccessException("User does not exist");  //400 when one of three does not exist in request
        } else if(!user.getPassword().equals(request.password())){
            throw new DataAccessException("Unauthorized");  //401 when uhhh uhmm
        }
        AuthData newAuth = new AuthData();
        newAuth.setUsername(request.username());
        newAuth.setAuthToken(UUID.randomUUID().toString());
        new MemoryAuthDAO().createAuth(newAuth);

        return new LoginResult(newAuth.getUsername(), newAuth.getAuthToken());
    }

    public void logout(String authToken) throws DataAccessException{
        new MemoryAuthDAO().deleteAuth(authToken);
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException{
        new MemoryAuthDAO().getAuth(authToken);
        Collection<GameData> games = new MemoryGameDAO().listGames();
        List<Game> resultsList = new ArrayList<>();
        for(GameData game : games){
            Game gameResult = new Game(game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName());
            resultsList.add(gameResult);
        }
        return new ListGamesResult(resultsList);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws DataAccessException{
        new MemoryAuthDAO().getAuth(authToken);
        GameData gameData = new GameData();
        gameData.setGameName(request.gameName()); // need 400 error somewhere

        int gameID = new Random().nextInt(Integer.MAX_VALUE);
        Collection<GameData> games = new MemoryGameDAO().listGames();
        for(GameData game : games){
            if(game.getGameID() == gameID){
                gameID = new Random().nextInt(Integer.MAX_VALUE);
            }
        }

        gameData.setGameID(gameID);

        new MemoryGameDAO().createGame(gameData);
        return new CreateGameResult(gameID);
    }

    public void joinGame(String authToken, JoinGameRequest request) throws DataAccessException{
        String username = new MemoryAuthDAO().getAuth(authToken).getUsername(); //401
        GameData game = new MemoryGameDAO().getGame(request.gameID());  //400

        String color = request.playerColor();
        if(color != null && color.equals("WHITE")){
            if(game.getWhiteUsername() != null){
                throw new DataAccessException("Already taken"); //403
            }
            game.setWhiteUsername(username);
        } else if(color != null && color.equals("BLACK")){
            if(game.getBlackUsername() != null){
                throw new DataAccessException("Already taken"); //403
            }
            game.setBlackUsername(username);
        } else {
            throw new DataAccessException("Bad Request"); //400
        }

        new MemoryGameDAO().updateGame(request.gameID(), game);
    }

}
