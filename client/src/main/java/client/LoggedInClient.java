package client;

import repl.Repl;

public class LoggedInClient extends Client {

    String authToken;
    String serverURL;
    Repl repl;

    public LoggedInClient(String serverURL, Repl repl, String authToken){
        this.authToken = authToken;

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
                //o\\o//o\\o//o\\COMMANDS//o\\o//o\\o//o\\
                help - see a list of commands
                quit - quit the program
                login <username> <password> - log in
                register <username> <password> <email> - create an account
                \\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//
                """;
    }

    public String logout() throws Exception{
        throw new Exception("not implemented");
    }

    public String createGame(String[] tokens) throws Exception{
        //name
        throw new Exception("not implemented");
    }

    public String listGames() throws Exception{
        throw new Exception("not implemented");
    }

    public String playGame(String[] tokens) throws Exception{
        // gameNum, color

        //repl.client = new GameplayClient(serverURL, repl);
        throw new Exception("not implemented"); //game num is not ID, it is the number in the list from the last time games were listed.
    }

    public String observeGame(String[] tokens) throws Exception{
        //gamenum
        throw new Exception("not implemented");
    }
}
