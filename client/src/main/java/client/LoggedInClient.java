package client;

public class LoggedInClient extends Client {

    public LoggedInClient(String serverURL, REPL repl){}

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
        throw new Exception("not implemented");
    }

    public String logout() throws Exception{
        throw new Exception("not implemented");
    }

    public String createGame(String name) throws Exception{
        throw new Exception("not implemented");
    }

    public String listGames() throws Exception{
        throw new Exception("not implemented");
    }

    public String playGame(String gameNum, String color) throws Exception{
        throw new Exception("not implemented"); //game num is not ID, it is the number in the list from the last time games were listed.
    }

    public String observeGame(String gameNum) throws Exception{
        throw new Exception("not implemented");
    }
}
