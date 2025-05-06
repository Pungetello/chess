package chess;

import java.util.Collection;
import java.util.LinkedList;

public class BishopMovesCalculator {

    public static Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new LinkedList<ChessMove>();
        ChessPosition possible = myPosition;
        while (true){ // up right
            possible = new ChessPosition(possible.getRow()+1, possible.getColumn()+1);
            if(possible.getRow() > 8 || possible.getColumn() > 8){
                break;
            }
            if(board.getBoard()[possible.getRow()-1][possible.getColumn()-1] != null){
                break;
            }
            moves.add(new ChessMove(myPosition, possible, null));
        }
        possible = myPosition;
        while (true){ // down right
            possible = new ChessPosition(possible.getRow()-1, possible.getColumn()+1);
            if(possible.getRow() < 1 || possible.getColumn() > 8){
                break;
            }
            if(board.getBoard()[possible.getRow()-1][possible.getColumn()-1] != null){
                break;
            }
            moves.add(new ChessMove(myPosition, possible, null));
        }
        possible = myPosition;
        while (true){ // up left
            possible = new ChessPosition(possible.getRow()+1, possible.getColumn()-1);
            if(possible.getRow() > 8 || possible.getColumn() < 1){
                break;
            }
            if(board.getBoard()[possible.getRow()-1][possible.getColumn()-1] != null){
                break;
            }
            moves.add(new ChessMove(myPosition, possible, null));
        }
        possible = myPosition;
        while (true){ // down left
            possible = new ChessPosition(possible.getRow()-1, possible.getColumn()-1);
            if(possible.getRow() < 1 || possible.getColumn() < 1){
                break;
            }
            if(board.getBoard()[possible.getRow()-1][possible.getColumn()-1] != null){
                break;
            }
            moves.add(new ChessMove(myPosition, possible, null));
        }
        return moves;
    }
}
