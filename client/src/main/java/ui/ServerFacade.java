package ui;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clearDatabase(){
        String path = "/db";
        this.makeRequest("DELETE", path, null);
    }

    public String register(String body){
        String path = "/user";
        return this.makeRequest("POST", path, body);
    }

    public String login(String body){
        String path = "/session";
        return this.makeRequest("POST", path, body);
    }

    public void logout(String body){
        String path = "/session";
        return this.makeRequest("DELETE", path, body);
    }

    public String listGames(String body){
        String path = "/game";
        return this.makeRequest("GET", path, body);
    }

    public String createGame(String body){
        String path = "/game";
        return this.makeRequest("POST", path, body);
    }

    public String joinGame(String body){
        String path = "/game";
        return this.makeRequest("PUT", path, body);
    }

    //o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\


    private String makeRequest(String method, String path, String body){
        return path;
    }


}
