package client;

public abstract class Client {

    public abstract String help();
    public abstract String eval(String line) throws Exception;

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception{
        throw new Exception("not implemented");
    }



}
