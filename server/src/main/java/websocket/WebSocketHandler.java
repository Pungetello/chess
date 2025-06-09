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
        String username;
        GameData game;
        String color;
        try {
            username = new SQLAuthDAO().getAuth(command.getAuthToken()).getUsername();
            game = new SQLGameDAO().getGame(command.getGameID());
            color = "observer";
            if (game.getWhiteUsername().equals(username)) {
                color = "white";
            } else if (game.getBlackUsername().equals(username)) {
                color = "black";
            }

            if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
                connect(username, session, command.getGameID(), game, color);
            }else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                makeMove(color, command.getGameID(), game);
            } else if (command.getCommandType() == UserGameCommand.CommandType.LEAVE){
                //do stuff
            } else if (command.getCommandType() == UserGameCommand.CommandType.RESIGN){
                //do stuff
            }
        } catch (DataAccessException ex){
            if (ex.getMessage().equals("Game does not exist")) {
                sendError(session, "Error: bad auth token");
            } else if (ex.getMessage().equals("Unauthorized")){
                sendError(session, "Error: invalid game ID");
            }
        }
    }

    private void connect(String username, Session session, int gameID, GameData game, String color) throws Exception {
        connections.add(username, session, gameID);

            var message = username + " has joined the game as " + color;
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification, gameID); //notify all in game, excluding the user
            ChessGame gameBoard = game.getGame();

            LoadGameMessage response = new LoadGameMessage(gameBoard);

            session.getRemote().sendString(new Gson().toJson(response));
    }

    private void makeMove(String color, int gameID, GameData data) throws Exception {
        String message = color + " makes a move";
        new SQLGameDAO().updateGame(gameID, data);
        var notification = new NotificationMessage(data.getGame().toString());//change the toString method to match the showBoard thing?
        connections.broadcast(null, notification, gameID);
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

    private void sendError(Session session, String errorMessage) throws Exception{
        ErrorMessage error = new ErrorMessage(errorMessage);
        session.getRemote().sendString(new Gson().toJson(error));
    }
}