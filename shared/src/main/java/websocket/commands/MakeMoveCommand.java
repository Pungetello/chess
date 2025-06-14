package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private ChessMove move;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.valueOf("MAKE_MOVE"), authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove(){
        return move;
    }


}
