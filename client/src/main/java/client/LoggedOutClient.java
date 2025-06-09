package client;
import repl.Repl;
import requests.*;
import results.*;
import ui.ServerFacade;
import exception.ResponseException;

public class LoggedOutClient extends Client {
    String serverURL;
    Repl repl;
    ServerFacade facade;

    public LoggedOutClient(String serverURL, Repl repl){
        facade = new ServerFacade(serverURL);
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
        } else if (command.equals("login")){
            return login(tokens);
        } else if (command.equals("register")){
            return register(tokens);
        } else {
            return "Command not recognized. Type 'help' for list of commands.";
        }
    }

    public String help() {
        return """
                
                //o\\o//o\\o//o\\o//o\\COMMANDS//o\\o//o\\o//o\\o//o\\
                help - see a list of commands
                quit - quit the program
                login <username> <password> - log in
                register <username> <password> <email> - create an account
                \\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//
                """;
    }

    public String login(String[] args) throws Exception{
        if(args.length != 3){
            return "Usage: login <username> <password>";
        }
        String username = args[1];
        String password = args[2];
        LoginRequest request = new LoginRequest(username, password);
        try{
            LoginResult result = facade.login(request);
            repl.client = new LoggedInClient(serverURL, repl, result.authToken());
            return "Successfully logged in as user " + result.username();
        } catch (ResponseException ex){
            int status = ex.statusCode();
            if (status == 401){
                return "Error: wrong password";
            } else if (status == 400){
                return "Error: please provide a username and password";
            } else {
                return "Error: unknown error";
            }
        }
    }

    public String register(String[] args) throws Exception{
        if(args.length != 4){
            return "Usage: register <username> <password> <email>";
        }
        String username = args[1];
        String password = args[2];
        String email = args[3];

        RegisterRequest request = new RegisterRequest(username, password, email);

        try {
            LoginResult result = new ServerFacade(serverURL).register(request);
            repl.client = new LoggedInClient(serverURL, repl, result.authToken());
            return "Successfully registered and logged in as user " + result.username();
        } catch (ResponseException ex){
            int status = ex.statusCode();
            if (status == 403){
                return "Error: that username is already taken";
            } else if (status == 400){
                return "Error: please provide a username, password, and email";
            } else {
                return "Error: unknown error";
            }
        }
    }


}
