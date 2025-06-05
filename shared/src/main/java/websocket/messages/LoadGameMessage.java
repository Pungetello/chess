package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    private ChessGame game; //could be a different variable type

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame(){
        return game;
    }
}
