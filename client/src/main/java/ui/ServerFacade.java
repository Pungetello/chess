package ui;

import dataaccess.DataAccessException;
import requests.*;
import results.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clearDatabase(){
        String path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public LoginResult register(RegisterRequest request){ // put in a RegisterRequest instead?
        String path = "/user";
        //String body = new Gson().toJson(request);
        return this.makeRequest("POST", path, request, LoginResult.class);
    }

    public LoginResult login(LoginRequest request){
        String path = "/session";
        String body = new Gson().toJson(request);
        return this.makeRequest("POST", path, body, LoginResult.class);
    }

    public void logout(String authToken){
        String path = "/session";
        return this.makeRequest("DELETE", path, null, null);
    }

    public ListGamesResult listGames(String authToken){
        String path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResult.class);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request){
        String path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public void joinGame(String authToken, JoinGameRequest request){
        String path = "/game";
        return this.makeRequest("PUT", path, request, null);
    }


    //o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
