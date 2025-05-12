package chess;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class KnightMoveCalculator {

    private Collection<ChessMove> moves;

    public KnightMoveCalculator(ChessBoard board, ChessPosition myPosition){
        this.moves = calculate(board, myPosition);
    }

    public Collection<ChessMove> getMoves(){
        return moves;
    }

    public Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> result = new LinkedList<ChessMove>();
        int[][] possibleMoves = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};

        for(int[] possibleMove : possibleMoves){
            ChessPosition possiblePosition = new ChessPosition (myPosition.getRow() + possibleMove[0], myPosition.getColumn() + possibleMove[1]);
            if(possiblePosition.getRow() > 0 && possiblePosition.getRow() < 9 && possiblePosition.getColumn() > 0 && possiblePosition.getColumn() < 9){
                if(board.getBoard()[possiblePosition.getRow()-1][possiblePosition.getColumn()-1] == null || board.getBoard()[possiblePosition.getRow()-1][possiblePosition.getColumn()-1].getTeamColor() != board.getBoard()[myPosition.getRow()-1][myPosition.getColumn()-1].getTeamColor()){
                    result.add(new ChessMove(myPosition, possiblePosition, null));
                }
            }
        }

        return result;
    }

}
