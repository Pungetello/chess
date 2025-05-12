package chess;

import java.util.Collection;
import java.util.LinkedList;

public class RookMoveCalculator {

    private Collection<ChessMove> moves;

    public RookMoveCalculator(ChessBoard board, ChessPosition myPosition){
        this.moves = calculate(board, myPosition);
    }

    public Collection<ChessMove> getMoves(){
        return moves;
    }

    public Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> result = new LinkedList<ChessMove>();
        ChessPosition possiblePosition;

        int[][] possibleDirections = {{-1,0},{1,0},{0,1},{0,-1}};

        for(int[] direction : possibleDirections) {
            possiblePosition = myPosition;
            while (true) {
                possiblePosition = new ChessPosition(possiblePosition.getRow() + direction[0], possiblePosition.getColumn() + direction[1]);

                if (possiblePosition.getRow() < 1 || possiblePosition.getRow() > 8 || possiblePosition.getColumn() < 1 || possiblePosition.getColumn() > 8) {
                    break;
                } else if (board.getPiece(possiblePosition) == null) {
                    result.add(new ChessMove(myPosition, possiblePosition, null));
                } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    result.add(new ChessMove(myPosition, possiblePosition, null));
                    break;
                } else {
                    break;
                }

            }
        }

        return result;
    }



}
