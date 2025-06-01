package client;

import repl.Repl;
import ui.ServerFacade;

public class GameplayClient extends Client {
    ServerFacade facade;
    String authToken;
    String serverURL;
    Repl repl;

    public GameplayClient(String serverURL, Repl repl, String authToken){
        facade = new ServerFacade(serverURL);
        this.authToken = authToken;
        this.repl = repl;
        this.serverURL = serverURL;
    }

    public String help() {
        return """
                //o\\o//o\\o//o\\o//o\\COMMANDS//o\\o//o\\o//o\\o//o\\
                help - see a list of commands
                quit - quit the program
                logout - log out
                create <game name> - creates a new game
                list - see a list of games in the database
                play <player color> <game number> - join a game and begin
                    playing, using number from list
                observe <game number> - observe an ongoing game
                \\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//
                """;
    }
}
