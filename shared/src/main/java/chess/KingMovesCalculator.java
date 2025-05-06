package chess;

import java.util.Collection;
import java.util.LinkedList;

public class KingMovesCalculator {

    public static Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new LinkedList<ChessMove>();
        ChessPosition iterator = myPosition;

        int[][] possibleMoves = {{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{-1,0},{0,1},{0,-1}};

        for(int[] possibleMove : possibleMoves) {
        iterator = new ChessPosition(myPosition.getRow()+possibleMove[0], myPosition.getColumn()+possibleMove[1]);
        if (iterator.getRow() <= 8 && iterator.getColumn() <= 8 && iterator.getRow() > 0 && iterator.getColumn() > 0 && board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        }
}
        return moves;
    }
}
