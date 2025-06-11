package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
        ChessGame.TeamColor color;
        try {
            username = new SQLAuthDAO().getAuth(command.getAuthToken()).getUsername();
            game = new SQLGameDAO().getGame(command.getGameID());
            color = null;
            if (game.getWhiteUsername().equals(username)) {
                color = ChessGame.TeamColor.WHITE;
            } else if (game.getBlackUsername().equals(username)) {
                color = ChessGame.TeamColor.BLACK;
            }

            if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
                connect(username, session, command.getGameID(), game, color);
            }else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(username, session, color, command.getGameID(), game, moveCommand.getMove());
            } else if (command.getCommandType() == UserGameCommand.CommandType.LEAVE){
                leave(username, session);
            } else if (command.getCommandType() == UserGameCommand.CommandType.RESIGN){
                resign(username, session);
            }
        } catch (DataAccessException ex){
            if (ex.getMessage().equals("Game does not exist")) {
                sendError(session, "Error: bad auth token");
            } else if (ex.getMessage().equals("Unauthorized")){
                sendError(session, "Error: invalid game ID");
            }
        }
    }

    private void connect(String username, Session session, int gameID, GameData game, ChessGame.TeamColor color) throws Exception {
        connections.add(username, session, gameID);

            var message = username + " has joined the game as " + color;
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification, gameID); //notify all in game, excluding the user
            ChessGame gameBoard = game.getGame();

            LoadGameMessage response = new LoadGameMessage(gameBoard);

            session.getRemote().sendString(new Gson().toJson(response));
    }

    private void makeMove(String username, Session session, ChessGame.TeamColor color, int gameID, GameData data, ChessMove move) throws Exception {
        try {
            ChessGame game = data.getGame();
            if(color==null){
                sendError(session, "Error: you are observing this game, you cannot make any moves");
                return;
            } else if (color != game.getTeamTurn()){
                sendError(session, "Error: it is not your turn");
                return;
            }
            game.makeMove(move);
            String message = color + " makes a move";
            new SQLGameDAO().updateGame(gameID, data);
            var gameMessage = new LoadGameMessage(data.getGame());
            connections.broadcast(null, gameMessage, gameID);
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification, gameID);

            checkSpecialConditions(game, color, gameID);

        } catch (InvalidMoveException ex){
            sendError(session, "Error: invalid move");
        }
    }

    private void checkSpecialConditions(ChessGame game, ChessGame.TeamColor color, int gameID) throws Exception{
        ChessGame.TeamColor otherTeam = ChessGame.TeamColor.WHITE;
        if(color == ChessGame.TeamColor.WHITE){
            otherTeam = ChessGame.TeamColor.BLACK;
        }

        if (game.isInCheckmate(otherTeam)){
            var checkMateNotification = new NotificationMessage(otherTeam + " is in checkmate!");
            connections.broadcast(null, checkMateNotification, gameID);
            //finish game
        } else if (game.isInStalemate(otherTeam)){
            var stalemateNotification = new NotificationMessage(otherTeam + " is in stalemate!");
            connections.broadcast(null, stalemateNotification, gameID);
            //finish game
        } else if (game.isInCheck(otherTeam)){
            var checkNotification = new NotificationMessage(otherTeam + " is in check!"); //maybe this is unproffessional to have, but as a bad chess player I'd want it
            connections.broadcast(null, checkNotification, gameID);
        }

    }

    private void leave(String username, Session session){

    }

    private void resign(String username, Session session){

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