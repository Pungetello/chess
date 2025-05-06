package chess;

import java.util.Collection;

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

    /**
     * @override equals
     */
    public boolean equals(ChessPiece other) {
        System.out.println(pieceColor + " " + pieceType + " " + other.pieceColor + " " + other.pieceType);
        return pieceColor.toString().equals(other.getTeamColor().toString()) && pieceType.toString().equals(other.getPieceType().toString());
    }

    /**
     * @Override hashCode
     */
    public int hashCode() {
        return this.toString().hashCode();
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
        if (pieceType == PieceType.QUEEN) {
            return RookMovesCalculator.calculate(board, myPosition).addAll(BishopMovesCalculator.calculate(board, myPosition));
        }
        if (pieceType == PieceType.KING) {
            return KingMovesCalculator.calculate(board, myPosition);
        }
        return null;
    }
}
