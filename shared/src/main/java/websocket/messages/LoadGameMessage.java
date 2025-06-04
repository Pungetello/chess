package websocket.messages;

public class LoadGameMessage extends ServerMessage{
    private var game;

    public LoadGameMessage(var game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public var getGame(){
        return game;
    }
}
