package chess;

import java.util.Collection;
import java.util.LinkedList;

public class BishopMovesCalculator {

    public static Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new LinkedList<ChessMove>();
        ChessPosition iterator = myPosition;
        while (true){ // up right
            iterator = new ChessPosition(iterator.getRow()+1, iterator.getColumn()+1);
            if(iterator.getRow() > 8 || iterator.getColumn() > 8){
                break;
            }
            if(board.getBoard()[iterator.getRow()][iterator.getColumn()] != null){
                break;
            }
            moves.add(new ChessMove(myPosition, iterator, null));
        }
        iterator = myPosition;
        while (true){ // down right
            iterator = new ChessPosition(iterator.getRow()-1, iterator.getColumn()+1);
            if(iterator.getRow() < 1 || iterator.getColumn() > 8){
                break;
            }
            if(board.getBoard()[iterator.getRow()][iterator.getColumn()] != null){
                break;
            }
            moves.add(new ChessMove(myPosition, iterator, null));
        }
        iterator = myPosition;
        while (true){ // up left
            iterator = new ChessPosition(iterator.getRow()+1, iterator.getColumn()-1);
            if(iterator.getColumn() < 1 || iterator.getRow() > 8){
                break;
            }
            if(board.getBoard()[iterator.getRow()][iterator.getColumn()] != null){
                break;
            }
            moves.add(new ChessMove(myPosition, iterator, null));
        }
        iterator = myPosition;
        while (true){ // down left
            iterator = new ChessPosition(iterator.getRow()-1, iterator.getColumn()-1);
            if(iterator.getColumn() < 1 || iterator.getRow() < 1){
                break;
            }
            if(board.getBoard()[iterator.getRow()][iterator.getColumn()] != null){
                break;
            }
            moves.add(new ChessMove(myPosition, iterator, null));
        }
        return moves;
    }
}
