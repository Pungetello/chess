package ui;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void help() throws Exception{
        throw new Exception("not implemented");
    }

    public void quit() throws Exception{
        throw new Exception("not implemented");
    }

    public void login(String username, String password) throws Exception{
        throw new Exception("not implemented");
    }

    public void register(String username, String password, String email) throws Exception{
        throw new Exception("not implemented");
    }

    public void logout() throws Exception{
        throw new Exception("not implemented");
    }

    public void createGame(String name) throws Exception{
        throw new Exception("not implemented");
    }

    public void listGames() throws Exception{
        throw new Exception("not implemented");
    }

    public void playGame(String gameNum, String color) throws Exception{
        throw new Exception("not implemented"); //game num is not ID, it is the number in the list from the last time games were listed.
    }

    public void observeGame(String gameNum) throws Exception{
        throw new Exception("not implemented");
    }


    //o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception{
        throw new Exception("not implemented");
    }


}
