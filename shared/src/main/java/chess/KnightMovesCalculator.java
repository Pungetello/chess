package chess;

import java.util.Collection;
import java.util.LinkedList;

public class KnightMovesCalculator {

    public static Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new LinkedList<ChessMove>();

        ChessPosition iterator = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1);
        if (iterator.getRow() <= 8 && iterator.getColumn() <= 8 && board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        }

        iterator = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2);
        if (iterator.getRow() <= 8 && iterator.getColumn() <= 8 && board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        }

        iterator = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1);
        if (iterator.getRow() <= 8 && iterator.getColumn() > 0 && board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        }

        iterator = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2);
        if (iterator.getRow() <= 8 && iterator.getColumn() > 0 && board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        }

        iterator = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1);
        if (iterator.getRow() > 0 && iterator.getColumn() <= 8 && board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        }

        iterator = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2);
        if (iterator.getRow() > 0 && iterator.getColumn() <= 8 && board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        }

        iterator = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1);
        if (iterator.getRow() > 0 && iterator.getColumn() > 0 && board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        }

        iterator = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2);
        if (iterator.getRow() > 0 && iterator.getColumn() > 0 && board.getBoard()[iterator.getRow()-1][iterator.getColumn()-1] == null) {
            moves.add(new ChessMove(myPosition, iterator, null));
        }

        return moves;
    }
}