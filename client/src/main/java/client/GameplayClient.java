package client;

import repl.Repl;
import results.Game;
import ui.ServerFacade;

public class GameplayClient extends Client {
    ServerFacade facade;
    String authToken;
    String serverURL;
    Repl repl;
    String playerColor;
    Game game;

    public GameplayClient(String serverURL, Repl repl, String authToken, String playerColor, Game game){
        facade = new ServerFacade(serverURL);
        this.authToken = authToken;
        this.repl = repl;
        this.serverURL = serverURL;
        this.playerColor = playerColor;
        this.game = game;
    }

    public String eval(String line) throws Exception {
        var tokens = line.toLowerCase().split(" ");
        var command = (tokens.length > 0) ? tokens[0] : "help";

        if (command.equals("help")){
            return help();
        } else if (command.equals("showBoard")){
            return showBoard();
        } else if (command.equals("exitGame")){
            return exitGame();
        } else if (command.equals("quit")){
            return "quit";
        } else {
            return help();
        }
    }

    public String exitGame() throws Exception{
        repl.client = new LoggedOutClient(serverURL, repl);
        return "Successfully exited game " + game.gameName();
    }

    public String help() {
        return """
                
                //o\\o//o\\o//o\\o//o\\COMMANDS//o\\o//o\\o//o\\o//o\\
                help - see a list of commands
                showBoard - display the current chessboard
                exitGame - exits the game
                quit - quit the program
                
                More commands to come once gameplay is implemented!
                \\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//
                """;
    }

    public String showBoard() {
        if (playerColor.equals("BLACK")){
            return printBlackBoard();
        } else {
            return printWhiteBoard();
        }

    }

    private String printBlackBoard(){
        return "Not yet implemented";
    }

    private String printWhiteBoard(){
        return "Not yet implemented";
    }
}
