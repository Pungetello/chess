package client;
import repl.Repl;
import requests.*;
import results.*;
import ui.ServerFacade;

public class LoggedOutClient extends Client {
    String serverURL;
    Repl repl;
    String authToken;

    public LoggedOutClient(String serverURL, Repl repl){

    }

    public String eval(String line) throws Exception {
        var tokens = line.toLowerCase().split(" ");
        var command = (tokens.length > 0) ? tokens[0] : "help";

        if (command.equals("help")){
            return help();
        } else if (command.equals("quit")){
            return "quit";
        } else if (command.equals("login")){
            return login(tokens);
        } else if (command.equals("register")){
            return register(tokens);
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

    public String login(String[] args) throws Exception{
        String username = args[1];
        String password = args[2];

        repl.client = new LoggedInClient(serverURL, repl);
        throw new Exception("not implemented");

    }

    public String register(String[] args) throws Exception{
        String username = args[1];
        String password = args[2];
        String email = args[3];

        RegisterRequest request = new RegisterRequest(username, password, email);
        LoginResult result = new ServerFacade(serverURL).register(request);
        authToken = result.authToken();
        return "Successfully registered user " + result.username();
    }


}
