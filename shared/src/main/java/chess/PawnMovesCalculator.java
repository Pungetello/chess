package chess;

import java.util.Collection;
import java.util.LinkedList;

public class PawnMovesCalculator {

    public static Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new LinkedList<ChessMove>();
        ChessPosition iterator = myPosition;

        //add two-space on first turn rule??

        iterator = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
        if (iterator.getRow() <= 7 && board.getBoard()[iterator.getRow()][iterator.getColumn()] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        } else if (iterator.getRow() == 8 && board.getBoard()[iterator.getRow()][iterator.getColumn()] == null) {
            moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.BISHOP));
        }

        return moves;
    }
}