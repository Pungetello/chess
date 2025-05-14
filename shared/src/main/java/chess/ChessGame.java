package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable{

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        for(ChessMove move : possibleMoves) {
            ChessGame possibleFuture = this.clone();
            possibleFuture.move(move);
            if (possibleFuture.isInCheck(piece.getTeamColor())){
                possibleMoves.remove(move);
            }
        }
            return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        for(ChessMove validMove : validMoves){
            if(move == validMove){
                move(move);
            }
        }
        throw new InvalidMoveException();
    }

    /**
     * Makes a chess move, not checking if it's valid
     * @param move chess move to perform
     */

    public void move(ChessMove move) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(move.getPromotionPiece() != null) {
            piece = new ChessPiece (piece.getTeamColor(), move.getPromotionPiece());
        }
        board.addPiece(move.getStartPosition(), null);
        board.addPiece(move.getEndPosition(), piece);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingsPosition = findKingsPosition(teamColor, board);
        for (int i = 0; i < board.getBoard().length; i++){
            for (int j = 0; j < board.getBoard()[i].length; j++){
                ChessPosition position = new ChessPosition(i+1,j+1);
                ChessPiece piece = board.getPiece(position);
                if(piece != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> possibleMoves = piece.pieceMoves(board, position); // if move would put other team in check, it still counts
                    for(ChessMove move : possibleMoves){
                        if(move.getEndPosition() == kingsPosition){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finds the position of the king of a given color
     *
     * @param teamColor which team to find the king of
     * @param board the board to check for the king in
     * @return the position of the first king of that color found, or null if none are found
     */

    public ChessPosition findKingsPosition(TeamColor teamColor, ChessBoard board){
        ChessPosition kingsPosition;
        for (int i = 0; i < board.getBoard().length; i++){
            for (int j = 0; j < board.getBoard()[i].length; j++){
                ChessPosition position = new ChessPosition(i+1,j+1);
                ChessPiece piece = board.getPiece(position);
                if(piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING){
                    kingsPosition = position;
                    return kingsPosition;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = validMoves(position);
                    if (!validMoves.isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    @Override
    public ChessGame clone(){
        try{
            ChessGame clone = (ChessGame) super.clone();

            ChessBoard clonedBoard = getBoard().clone();
            clone.setBoard(clonedBoard);
            return clone;

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
