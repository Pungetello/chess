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
                leave(username, color, command.getGameID());
            } else if (command.getCommandType() == UserGameCommand.CommandType.RESIGN){
                resign(username, session, command.getGameID(), color);
            }
        } catch (DataAccessException ex){
            if (ex.getMessage().equals("Game does not exist")) {
                sendError(session, "Error: bad auth token");
            } else if (ex.getMessage().equals("Unauthorized")){
                sendError(session, "Error: invalid game ID");
            }
        }
    }

    private void connect(String username, Session session, int gameID, GameData game, ChessGame.TeamColor teamColor) throws Exception {
        connections.add(username, session, gameID);
        String color;
        if(teamColor == null){
            color = "observer";
        } else {
            color = teamColor.toString().toLowerCase();
        }

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
            if(color == null){
                sendError(session, "Error: you are observing this game, you cannot make any moves");
                return;
            } else if (color != game.getTeamTurn()){
                sendError(session, "Error: it is not your turn");
                return;
            }
            game.makeMove(move);
            data.setGame(game);
            new SQLGameDAO().updateGame(gameID, data);

            var gameMessage = new LoadGameMessage(data.getGame());
            connections.broadcast(null, gameMessage, gameID);

            String message = color + " makes a move";
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification, gameID);

            checkSpecialConditions(game, color, gameID);

        } catch (InvalidMoveException ex){
            sendError(session, "Error: invalid move");
        }
    }

    private void checkSpecialConditions(ChessGame game, ChessGame.TeamColor color, int gameID) throws Exception{
        ChessGame.TeamColor otherColor;
        String otherUsername;
        if(color == ChessGame.TeamColor.WHITE){
            otherUsername = new SQLGameDAO().getGame(gameID).getBlackUsername();
            otherColor = ChessGame.TeamColor.BLACK;
        }else{
            otherUsername = new SQLGameDAO().getGame(gameID).getWhiteUsername();
            otherColor = ChessGame.TeamColor.WHITE;
        }

        if (game.isInCheckmate(otherColor)){
            var checkMateNotification = new NotificationMessage(otherUsername + " is in checkmate!");
            connections.broadcast(null, checkMateNotification, gameID);
            endGame(gameID, otherColor);
        } else if (game.isInStalemate(otherColor)){
            var stalemateNotification = new NotificationMessage(otherUsername + " is in stalemate!");
            connections.broadcast(null, stalemateNotification, gameID);
            endGame(gameID, otherColor);
        } else if (game.isInCheck(otherColor)){
            var checkNotification = new NotificationMessage(otherUsername + " is in check!");
            connections.broadcast(null, checkNotification, gameID);
        }
    }

    private void endGame(int gameID, ChessGame.TeamColor loserColor) throws Exception{
        SQLGameDAO dao = new SQLGameDAO();
        GameData data = dao.getGame(gameID);
        ChessGame game = data.getGame();
        game.closeGame();
        data.setGame(game);
        dao.updateGame(gameID, data);
        NotificationMessage notification = new NotificationMessage(loserColor.toString().toLowerCase() + " wins!");
        connections.broadcast(null, notification, gameID);
    }

    private void leave(String username, ChessGame.TeamColor color, int gameID) throws Exception{
        connections.remove(username);

        SQLGameDAO dao = new SQLGameDAO();
        GameData data = dao.getGame(gameID);
        if(color == ChessGame.TeamColor.WHITE){
            data.setWhiteUsername(null);
        } else if (color == ChessGame.TeamColor.BLACK){
            data.setBlackUsername(null);
        }
        dao.updateGame(gameID, data);

        NotificationMessage notification = new NotificationMessage(username + " has left the game.");
        connections.broadcast(null, notification, gameID);
    }

    private void resign(String username, Session session, int gameID, ChessGame.TeamColor color) throws Exception{
        if (color == null){
            sendError(session, "Error: type 'leave' to stop observing the game");
            return;
        }

        NotificationMessage notification = new NotificationMessage(username + " has resigned!");
        connections.broadcast(null, notification, gameID);
        endGame(gameID, color);
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