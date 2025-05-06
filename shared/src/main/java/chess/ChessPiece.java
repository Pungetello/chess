package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * @override toString
     */
    public String toString() {
        return pieceType.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (pieceType == PieceType.ROOK) {
            return RookMovesCalculator.calculate(board, myPosition);
        }
        if (pieceType == PieceType.KNIGHT) {
            return KnightMovesCalculator.calculate(board, myPosition);
        }
        if (pieceType == PieceType.BISHOP) {
            return BishopMovesCalculator.calculate(board, myPosition);
        }
        //if (pieceType == PieceType.QUEEN) {
            //return RookMovesCalculator.calculate(board, myPosition).addAll(BishopMovesCalculator.calculate(board, myPosition));
        //}
        if (pieceType == PieceType.KING) {
            return KingMovesCalculator.calculate(board, myPosition);
        }
        return null;
    }
}
