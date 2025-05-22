package chess;

import java.util.Collection;
import java.util.LinkedList;

public class OneStepMovesCalculator {

    private Collection<ChessMove> moves;

    public OneStepMovesCalculator(ChessBoard board, ChessPosition myPosition, int[][] possibleMoves){
        this.moves = calculate(board, myPosition, possibleMoves);
    }

    public Collection<ChessMove> getMoves(){
        return moves;
    }

    public Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition, int[][] possibleMoves){
        Collection<ChessMove> result = new LinkedList<ChessMove>();

        for(int[] possibleMove : possibleMoves){
            ChessPosition possiblePosition = new ChessPosition (myPosition.getRow() + possibleMove[0], myPosition.getColumn() + possibleMove[1]);
            if(possiblePosition.inRange()){
                ChessPiece myPiece = board.getPiece(myPosition);
                ChessPiece possiblePieceToTake = board.getPiece(possiblePosition);
                if(possiblePieceToTake == null || possiblePieceToTake.getTeamColor() != myPiece.getTeamColor()){
                    result.add(new ChessMove(myPosition, possiblePosition, null));
                }
            }
        }
        
        return result;
    }

}
