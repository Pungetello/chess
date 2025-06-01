package service;
import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import requests.*;
import results.*;

import java.util.*;

public class Service {
    public void clear() throws DataAccessException {
        new SQLAuthDAO().clear();
        new SQLGameDAO().clear();
        new SQLUserDAO().clear();
    }

    public LoginResult register(RegisterRequest request) throws DataAccessException{
        if (request.username() == null || request.email() == null || request.password() == null){
            throw new DataAccessException("bad request"); //400
        }
        SQLUserDAO userDataAccess = new SQLUserDAO();
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
        new SQLAuthDAO().createAuth(newAuth);

        return new LoginResult(newAuth.getUsername(), newAuth.getAuthToken());
    }

    public LoginResult login(LoginRequest request) throws DataAccessException{
        SQLUserDAO userDataAccess = new SQLUserDAO();
        UserData user = userDataAccess.getUser(request.username());

        if (request.username() == null || request.password() == null){
            throw new DataAccessException("User does not exist");  //400
        } else if(user == null || !BCrypt.checkpw(request.password(), user.getPassword())){
            throw new DataAccessException("Unauthorized");  //401
        }
        AuthData newAuth = new AuthData();
        newAuth.setUsername(request.username());
        newAuth.setAuthToken(UUID.randomUUID().toString());
        new SQLAuthDAO().createAuth(newAuth);

        return new LoginResult(newAuth.getUsername(), newAuth.getAuthToken());
    }

    public void logout(String authToken) throws DataAccessException{
        new SQLAuthDAO().deleteAuth(authToken);
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException{
        new SQLAuthDAO().getAuth(authToken);
        Collection<GameData> games = new SQLGameDAO().listGames();
        List<Game> resultsList = new ArrayList<>();
        for(GameData game : games){
            Game gameResult = new Game(game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName());
            resultsList.add(gameResult);
        }
        return new ListGamesResult(resultsList);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws DataAccessException{
        if(request.gameName() == null){
            throw new DataAccessException("Bad request"); // 400
        }
        new SQLAuthDAO().getAuth(authToken);
        GameData gameData = new GameData();
        gameData.setGameName(request.gameName());

        int gameID = new Random().nextInt(Integer.MAX_VALUE);
        Collection<GameData> games = new SQLGameDAO().listGames();
        for(GameData game : games){
            if(game.getGameID() == gameID){
                gameID = new Random().nextInt(Integer.MAX_VALUE);
            }
        }

        gameData.setGameID(gameID);

        new SQLGameDAO().createGame(gameData);
        return new CreateGameResult(gameID);
    }

    public void joinGame(String authToken, JoinGameRequest request) throws DataAccessException{
        String username = new SQLAuthDAO().getAuth(authToken).getUsername(); //401
        GameData game = new SQLGameDAO().getGame(request.gameID());  //400

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

        new SQLGameDAO().updateGame(request.gameID(), game);
    }

}
