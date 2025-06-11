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
            if (game.getWhiteUsername() != null && game.getWhiteUsername().equals(username)) {
                color = ChessGame.TeamColor.WHITE;
            } else if (game.getBlackUsername() != null && game.getBlackUsername().equals(username)) {
                color = ChessGame.TeamColor.BLACK;
            }

            if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
                connect(username, session, command.getGameID(), game, color);
            }else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(username, session, color, command.getGameID(), game, moveCommand.getMove());
            } else if (command.getCommandType() == UserGameCommand.CommandType.LEAVE){
                leave(username, session, color, command.getGameID());
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
            }
            if (game.isOver()){
                sendError(session, "Error: game has ended");
                return;
            }
            if (color != game.getTeamTurn()){
                sendError(session, "Error: it is not your turn");
                return;
            }
            game.makeMove(move);
            data.setGame(game);
            new SQLGameDAO().updateGame(gameID, data);

            var gameMessage = new LoadGameMessage(data.getGame());
            connections.broadcast(null, gameMessage, gameID);

            String message = username + " moves "; // make it have more info
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
            var checkMateNotification = new NotificationMessage(otherUsername + " is in checkmate, " + color.toString().toLowerCase() + " wins!");
            connections.broadcast(null, checkMateNotification, gameID);
            endGame(gameID);
        } else if (game.isInStalemate(otherColor)){
            var stalemateNotification = new NotificationMessage(otherUsername + " is in stalemate, " + color.toString().toLowerCase() + " wins!");
            connections.broadcast(null, stalemateNotification, gameID);
            endGame(gameID);
        } else if (game.isInCheck(otherColor)){
            var checkNotification = new NotificationMessage(otherUsername + " is in check");
            connections.broadcast(null, checkNotification, gameID);
        }
    }

    private void endGame(int gameID) throws Exception{
        SQLGameDAO dao = new SQLGameDAO();
        GameData data = dao.getGame(gameID);
        ChessGame game = data.getGame();
        game.closeGame();
        data.setGame(game);
        dao.updateGame(gameID, data);
    }

    private void leave(String username, Session session, ChessGame.TeamColor color, int gameID) throws Exception{
        if(!connections.contains(username)){
            sendError(session, "Error: you have left the game");
            return;
        }

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

        if (new SQLGameDAO().getGame(gameID).getGame().isOver()){
            sendError(session, "Error: game has ended");
            return;
        }

        String winner = "Black";
        if (color == ChessGame.TeamColor.WHITE){
            winner = "White";
        }

        NotificationMessage notification = new NotificationMessage(username + " has resigned. " + winner + " wins!");
        connections.broadcast(null, notification, gameID);
        endGame(gameID);
    }

    private void sendError(Session session, String errorMessage) throws Exception{
        ErrorMessage error = new ErrorMessage(errorMessage);
        session.getRemote().sendString(new Gson().toJson(error));
    }
}