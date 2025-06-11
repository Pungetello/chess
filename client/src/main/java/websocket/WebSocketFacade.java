package websocket;

import chess.ChessMove;
import client.GameplayClient;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.*;
import websocket.messages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    GameplayClient client;


    public WebSocketFacade(GameplayClient client, String url, NotificationHandler notificationHandler) throws ResponseException { // Wherever this is created, it will need to pass in a notification handler, which can be the REPL, as that's what notifies people.
        try {
            this.client = client;
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                        notificationHandler.notify(notification);
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        LoadGameMessage loadGame = new Gson().fromJson(message, LoadGameMessage.class);
                        client.setBoard(loadGame.getGame().getBoard());
                        client.showBoard(loadGame.getGame().getBoard());
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                        ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
                        notificationHandler.notify(new NotificationMessage(error.getMessage()));//what's even the point of having two classes.
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    //leave and resign

    public void connect(String authToken, int gameID) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try{
            var command = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}
