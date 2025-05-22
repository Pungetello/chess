package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable{
    private ChessGame.TeamColor color;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
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
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.KING){
           return new OneStepMovesCalculator(board, myPosition, new int[][] {{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{0,1},{-1,0},{0,-1}}).getMoves();
        }
        if (type == PieceType.KNIGHT){
            return new OneStepMovesCalculator(board, myPosition, new int[][] {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}}).getMoves();
        }
        if (type == PieceType.BISHOP){
            return new DirectionalMovesCalculator(board, myPosition, new int[][] {{-1,-1},{1,-1},{-1,1},{1,1}}).getMoves();
        }
        if (type == PieceType.ROOK){
            return new DirectionalMovesCalculator(board, myPosition, new int[][] {{-1,0},{1,0},{0,1},{0,-1},}).getMoves();
        }
        if (type == PieceType.QUEEN){
            return new DirectionalMovesCalculator(board, myPosition, new int[][] {{-1,0},{1,0},{0,1},{0,-1},{-1,-1},{1,-1},{-1,1},{1,1}}).getMoves();
        }
        if(type == PieceType.PAWN){
            return new PawnMovesCalculator(board, myPosition).getMoves();
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public ChessPiece clone(){
        try{
            ChessPiece clone = (ChessPiece) super.clone();
            return clone;

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}


