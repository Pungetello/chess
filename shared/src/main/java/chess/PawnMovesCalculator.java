package chess;

import java.util.Collection;
import java.util.LinkedList;

public class PawnMovesCalculator {

    public static Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new LinkedList<ChessMove>();
        ChessPosition iterator = myPosition;

        if(board.getBoard()[myPosition.getRow()-1][myPosition.getColumn()-1].getTeamColor() == ChessGame.TeamColor.WHITE) {

            iterator = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            // if null in front, can move forward
            if (iterator.getRow() <= 8 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] == null) {
                if (myPosition.getRow() == 7 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] == null) { // if near end and null in front, becomes new piece
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, iterator, null));
                }
            }
            // if null both tiles in front and still on row 2, can move twice
            if (myPosition.getRow() == 2 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] == null && board.getBoard()[iterator.getRow()][iterator.getColumn() - 1] == null) {
                iterator = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                moves.add(new ChessMove(myPosition, iterator, null));
            }
            //Take piece right?
            iterator = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (iterator.getRow() <= 8 && iterator.getColumn() <= 8 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] != null && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1].getTeamColor() != board.getBoard()[myPosition.getRow() - 1][myPosition.getColumn() - 1].getTeamColor()) {
                if (myPosition.getRow() == 7) { // if also near end, becomes new piece
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, iterator, null));
                }

            }
            //Take piece left?
            iterator = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (iterator.getRow() <= 8 && iterator.getColumn() > 0 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] != null && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1].getTeamColor() != board.getBoard()[myPosition.getRow() - 1][myPosition.getColumn() - 1].getTeamColor()) {
                if (myPosition.getRow() == 7) { // if also near end, becomes new piece
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, iterator, null));
                }
            }
        }

        else if(board.getBoard()[myPosition.getRow()-1][myPosition.getColumn()-1].getTeamColor() == ChessGame.TeamColor.BLACK) {

            iterator = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            // if null in front, can move forward
            if (iterator.getRow() > 0 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] == null) {
                if (myPosition.getRow() == 2 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] == null) { // if near end and null in front, becomes new piece
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, iterator, null));
                }
            }
            // if null both tiles in front and still on row 2, can move twice
            if (myPosition.getRow() == 7 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] == null && board.getBoard()[iterator.getRow() - 2][iterator.getColumn() - 1] == null) {
                iterator = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                moves.add(new ChessMove(myPosition, iterator, null));
            }
            //Take piece right?
            iterator = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if (iterator.getRow() > 0 && iterator.getColumn() <= 8 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] != null && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1].getTeamColor() != board.getBoard()[myPosition.getRow() - 1][myPosition.getColumn() - 1].getTeamColor()) {
                if (myPosition.getRow() == 2) { // if also near end, becomes new piece
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, iterator, null));
                }

            }
            //Take piece left?
            iterator = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if (iterator.getRow() > 0 && iterator.getColumn() > 0 && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1] != null && board.getBoard()[iterator.getRow() - 1][iterator.getColumn() - 1].getTeamColor() != board.getBoard()[myPosition.getRow() - 1][myPosition.getColumn() - 1].getTeamColor()) {
                if (myPosition.getRow() == 2) { // if also near end, becomes new piece
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, iterator, ChessPiece.PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, iterator, null));
                }
            }
        }


        return moves;
    }
}