package chess;

import java.util.Collection;
import java.util.LinkedList;

public class KnightMovesCalculator {

    public static Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new LinkedList<ChessMove>();

        ChessPosition iterator;

        int[][] possibleMoves = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{-1,2},{1,-2},{-1,-2}};

        for(int[] possibleMove : possibleMoves) {
            iterator = new ChessPosition(myPosition.getRow() + possibleMove[0], myPosition.getColumn() + possibleMove[1]);
            if (iterator.getRow() <= 8 && iterator.getColumn() <= 8 && iterator.getRow() > 0 && iterator.getColumn() > 0) {
                if (board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] == null || board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1].getTeamColor() != board.getBoard()[myPosition.getRow() - 1][myPosition.getColumn() - 1].getTeamColor()) {
                    moves.add(new ChessMove(myPosition, iterator, null));
                }
            }
        }

        return moves;
    }
}