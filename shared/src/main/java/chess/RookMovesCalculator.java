package chess;

import java.util.Collection;
import java.util.LinkedList;

public class RookMovesCalculator {

    public static Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new LinkedList<ChessMove>();
        ChessPosition iterator = myPosition;
        while (true){ // right
            iterator = new ChessPosition(iterator.getRow()+1, iterator.getColumn());
            if(iterator.getRow() > 8){
                break;
            }
            if(board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] != null){
                if(board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1].getTeamColor() != board.getBoard()[myPosition.getRow()-1][myPosition.getColumn()-1].getTeamColor()){
                    moves.add(new ChessMove(myPosition, iterator, null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, iterator, null));
        }
        iterator = myPosition;
        while (true){ // left
            iterator = new ChessPosition(iterator.getRow()-1, iterator.getColumn());
            if(iterator.getRow() < 1){
                break;
            }
            if(board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] != null){
                if(board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1].getTeamColor() != board.getBoard()[myPosition.getRow()-1][myPosition.getColumn()-1].getTeamColor()){
                    moves.add(new ChessMove(myPosition, iterator, null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, iterator, null));
        }
        iterator = myPosition;
        while (true){ // up
            iterator = new ChessPosition(iterator.getRow(), iterator.getColumn()+1);
            if(iterator.getColumn() > 8){
                break;
            }
            if(board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] != null){
                if(board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1].getTeamColor() != board.getBoard()[myPosition.getRow()-1][myPosition.getColumn()-1].getTeamColor()){
                    moves.add(new ChessMove(myPosition, iterator, null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, iterator, null));
        }
        iterator = myPosition;
        while (true){ // down
            iterator = new ChessPosition(iterator.getRow(), iterator.getColumn()-1);
            if(iterator.getColumn() < 1){
                break;
            }
            if(board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] != null){
                if(board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1].getTeamColor() != board.getBoard()[myPosition.getRow()-1][myPosition.getColumn()-1].getTeamColor()){
                    moves.add(new ChessMove(myPosition, iterator, null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, iterator, null));
        }
        return moves;
    }
}
