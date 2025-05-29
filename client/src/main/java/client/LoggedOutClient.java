package client;

public class LoggedOutClient extends Client {
    String serverURL;
    Repl repl;

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
        throw new Exception("not implemented");
    }

    public String login(String[] args) throws Exception{
        String username = args[1];
        String password = args[2];

        throw new Exception("not implemented");
    }

    public String register(String[] args) throws Exception{
        String username = args[1];
        String password = args[2];
        String email = args[3];

        throw new Exception("not implemented");
    }


}
