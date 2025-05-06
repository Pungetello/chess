package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPosition[][] board = new ChessPosition[8][8];
    private ChessPiece[] pieces = new ChessPiece[32];

    public ChessBoard() {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++){
                board[i][j] = new ChessPosition(i,j);
            }
        }
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        piece.setPosition(position);
        for (ChessPiece p :pieces){
            if(p == null){
                p = piece;
                break;
            }
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        for (ChessPiece p :pieces){
            if(p.getPosition() == position){
                return p;
            }
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
