package client;

import exception.ResponseException;
import repl.Repl;
import results.*;
import requests.*;
import ui.ServerFacade;
import java.util.List;

public class LoggedInClient extends Client {
    ServerFacade facade;
    String authToken;
    String serverURL;
    Repl repl;
    List<Game> listedGames;

    public LoggedInClient(String serverURL, Repl repl, String authToken){
        facade = new ServerFacade(serverURL);
        this.authToken = authToken;
        this.repl = repl;
        this.serverURL = serverURL;
    }

    public String eval(String line) throws Exception {
        var tokens = line.toLowerCase().split(" ");
        var command = (tokens.length > 0) ? tokens[0] : "help";

        if (command.equals("help")){
            return help();
        } else if (command.equals("quit")){
            return "quit";
        } else if (command.equals("logout")){
            return logout();
        } else if (command.equals("create")){
            return createGame(tokens);
        } else if (command.equals("list")){
            return listGames();
        } else if (command.equals("play")){
            return playGame(tokens);
        } else if (command.equals("observe")){
            return observeGame(tokens);
        } else {
            return help();
        }
    }

    public String help() {
        return """
                
                //o\\o//o\\o//o\\o//o\\COMMANDS//o\\o//o\\o//o\\o//o\\
                help - see a list of commands
                quit - quit the program
                logout - log out
                create <game name> - creates a new game
                list - see a list of games in the database
                play <game number> <player color> - join a game and begin
                    playing, using number from list
                observe <game number> - observe an ongoing game
                \\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//
                """;
    }

    public String logout() throws Exception{
        repl.client = new LoggedOutClient(serverURL, repl);
        return "Successfully logged out";
    }

    public String createGame(String[] tokens) throws Exception{
        if(tokens.length != 2){
            return "Usage: create <game name>";
        }
        String name = tokens[1];
        CreateGameRequest request = new CreateGameRequest(name);
        try{
            facade.createGame(authToken, request);
            return "Successfully created game: " + name;
        } catch (ResponseException ex){
            return "Error: please provide game name";
        }
    }

    public String listGames() throws Exception{
        ListGamesResult result = facade.listGames(authToken);
        listedGames = result.games();
        return printListedGames();
    }

    private String printListedGames(){
        StringBuilder result = new StringBuilder();
        int i = 0;
        if (listedGames.isEmpty()){
            return "No games in the database to list";
        }
        for (Game game : listedGames){
            result.append(i)
                    .append(": ")
                    .append(game.gameName())
                    .append(", White player: ");
            if(game.whiteUsername() != null){
                result.append(game.whiteUsername());
            } else {
                result.append("none");
            }
            result.append(", Black player: ");
            if(game.blackUsername() != null){
                result.append(game.blackUsername());
            } else {
                result.append("none");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public String playGame(String[] tokens) throws Exception{
        if(tokens.length != 3){
            return "Usage: play <game number> <player color>";
        }
        int gameNum = Integer.parseInt(tokens[1]);
        String color = tokens[2].toUpperCase();
        int gameID = listedGames.get(gameNum).gameID();

        JoinGameRequest request = new JoinGameRequest(color, gameID);
        try{
            facade.joinGame(authToken,request);
            repl.client = new GameplayClient(serverURL, repl, authToken, color);
            return "Successfully joined game " + listedGames.get(gameNum).gameName() + " as " + color;
        } catch (ResponseException ex){
            int status = ex.StatusCode();
            if (status == 403){
                return "Error: that color is already taken";
            } else if (status == 400){
                return "Error: please provide a valid game number (from the list) and color (black or white)";
            } else if (status == 401){
                return "Error: unauthorized. Please log in, I guess?";
            } else {
                return "Error: unknown error";
            }
        }
    }

    public String observeGame(String[] tokens) throws Exception{
        if(tokens.length != 2){
            return "Usage: observe <game number>";
        }
        int gameNum = Integer.parseInt(tokens[1]);
        String color = "OBSERVER";
        int gameID = listedGames.get(gameNum).gameID(); //what to do with this?

        repl.client = new GameplayClient(serverURL, repl, authToken, color);
        return "Now observing game " + listedGames.get(gameNum).gameName();
    }
}
