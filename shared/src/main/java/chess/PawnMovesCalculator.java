package chess;

import java.util.Collection;
import java.util.LinkedList;

public class PawnMovesCalculator {

    private Collection<ChessMove> moves;

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition){
        this.moves = calculate(board, myPosition);
    }

    public Collection<ChessMove> getMoves(){
        return moves;
    }

    public Collection<ChessMove> calculate(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> result = new LinkedList<ChessMove>();

        int startRow = 7;
        int endRow = 2;
        int move = -1;

        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            startRow = 2;
            endRow = 7;
            move = 1;
        }

        ChessPosition possiblePosition = new ChessPosition(myPosition.getRow()+move, myPosition.getColumn());
        //if space in front null,
        if(board.getPiece(possiblePosition) == null) {
            //if current row end row
            if(myPosition.getRow() == endRow){
                addPromotionMoves(result, myPosition, possiblePosition);
            } else{
                result.add(new ChessMove(myPosition, possiblePosition, null));
            }
            //if current row is start row, and space move*2 null, add move twice
            possiblePosition = new ChessPosition(myPosition.getRow()+move+move, myPosition.getColumn());
            if(myPosition.getRow() == startRow && board.getPiece(possiblePosition) == null){
                result.add(new ChessMove(myPosition, possiblePosition, null));
            }
        }
        int[] directions = {1,-1};

        for(int direction : directions) {
            possiblePosition = new ChessPosition(myPosition.getRow() + move, myPosition.getColumn() + direction);
            if (possiblePosition.inRange() && board.getPiece(possiblePosition) != null && board.getPiece(possiblePosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                if (myPosition.getRow() == endRow) {
                    addPromotionMoves(result, myPosition, possiblePosition);
                } else {
                    result.add(new ChessMove(myPosition, possiblePosition, null));
                }
            }
        }

        return result;
    }

    private void addPromotionMoves(Collection<ChessMove> result, ChessPosition myPosition, ChessPosition possiblePosition){
        result.add(new ChessMove(myPosition, possiblePosition, ChessPiece.PieceType.QUEEN));
        result.add(new ChessMove(myPosition, possiblePosition, ChessPiece.PieceType.ROOK));
        result.add(new ChessMove(myPosition, possiblePosition, ChessPiece.PieceType.BISHOP));
        result.add(new ChessMove(myPosition, possiblePosition, ChessPiece.PieceType.KNIGHT));
    }
}