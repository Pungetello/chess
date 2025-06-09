package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
//import ui.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.*;
import websocket.commands.*;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT){
            String username = new SQLAuthDAO().getAuth(command.getAuthToken()).getUsername();
            connect(username, session, command.getGameID());
        }//else if (the other command types)
    }

    private void connect(String username, Session session, int gameID) throws Exception {
        connections.add(username, session, gameID);
        GameData game = new SQLGameDAO().getGame(gameID);
        String color = "observer";
        if (game.getWhiteUsername().equals(username)){
            color = "white";
        } else if (game.getBlackUsername().equals(username)){
            color = "black";
        }
        var message = username + " has joined the game as " + color;
        var notification = new NotificationMessage(message);
        connections.broadcast(username, notification, gameID); //notify all in game, excluding the user
        ChessGame gameBoard = game.getGame();

        LoadGameMessage response = new LoadGameMessage(gameBoard);

        session.getRemote().sendString(new Gson().toJson(response));
    }

    /*private void exit(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }

    public void makeNoise(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }*/
}