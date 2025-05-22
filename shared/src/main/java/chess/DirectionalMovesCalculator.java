package chess;

import java.util.Collection;
import java.util.LinkedList;

public class DirectionalMovesCalculator {

    private Collection<ChessMove> moves;

    public DirectionalMovesCalculator(ChessBoard board, ChessPosition myPosition, int[][] possibleDirections){
        this.moves = calculate(board, myPosition, possibleDirections);
    }

    public Collection<ChessMove> getMoves(){
        return moves;
    }

    public Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition, int[][] possibleDirections){
        Collection<ChessMove> result = new LinkedList<ChessMove>();
        ChessPosition possiblePosition;


        for(int[] direction : possibleDirections) {
            possiblePosition = myPosition;
            while (true) {
                possiblePosition = new ChessPosition(possiblePosition.getRow() + direction[0], possiblePosition.getColumn() + direction[1]);

                if (!possiblePosition.inRange()) {
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
