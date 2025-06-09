package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class Connection {
    public String username;
    public int gameID;
    public Session session;

    public Connection(String visitorName, Session session, int gameID) {
        this.username = visitorName;
        this.gameID = gameID;
        this.session = session;
    }

    public void send(NotificationMessage msg) throws IOException {
        session.getRemote().sendString(new Gson().toJson(msg));
    }
}