package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPiece.*;
import chess.ChessPosition;
import repl.Repl;
import results.Game;
import ui.ServerFacade;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.WHITE_KING;
import static ui.EscapeSequences.WHITE_ROOK;

public class GameplayClient extends Client {
    ServerFacade facade;
    String authToken;
    String serverURL;
    Repl repl;
    String playerColor;
    Game game;
    String BG_COLOR = SET_BG_COLOR_DARK_GREY;
    String DARK_SQUARE = SET_BG_COLOR_BLACK;
    String LIGHT_SQUARE = SET_BG_COLOR_LIGHT_GREY;
    String COORDS_COLOR = SET_TEXT_COLOR_BLACK;

    public GameplayClient(String serverURL, Repl repl, String authToken, String playerColor, Game game){
        facade = new ServerFacade(serverURL);
        this.authToken = authToken;
        this.repl = repl;
        this.serverURL = serverURL;
        this.playerColor = playerColor;
        this.game = game;
    }

    public String eval(String line) throws Exception {
        var tokens = line.toLowerCase().split(" ");
        var command = (tokens.length > 0) ? tokens[0] : "help";

        if (command.equals("help")){
            return help();
        } else if (command.equals("showBoard")){
            return showBoard(new ChessBoard());
        } else if (command.equals("exitGame")){
            return exitGame();
        } else if (command.equals("quit")){
            return "quit";
        } else {
            return help();
        }
    }

    public String exitGame() throws Exception{
        repl.client = new LoggedOutClient(serverURL, repl);
        return "Successfully exited game " + game.gameName();
    }

    public String help() {
        return """
                
                //o\\o//o\\o//o\\o//o\\COMMANDS//o\\o//o\\o//o\\o//o\\
                help - see a list of commands
                showBoard - display the current chessboard
                changeColors <color scheme> - change the color scheme of the board.
                    options: greyscale, sepia, neon.
                exitGame - exits the game
                quit - quit the program
                
                More commands to come once gameplay is implemented!
                \\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//
                """;
    }

    public String showBoard(ChessBoard board) {
        if (playerColor.equals("BLACK")){
            return printBlackBoard(board);
        } else {
            return printWhiteBoard(board);
        }

    }

    private String printBlackBoard(ChessBoard board){
        StringBuilder result = new StringBuilder();
        result.append(BG_COLOR)
                .append(COORDS_COLOR)
                .append(blackXAxis());
        for(int i=1; i <= 8; i++){
            result.append(rowAsString(i, board));
        }
        result.append(blackXAxis());
        return result.toString();
    }

    private String printWhiteBoard(ChessBoard board){
        StringBuilder result = new StringBuilder();
        result.append(BG_COLOR)
                .append(COORDS_COLOR)
                .append(whiteXAxis());
        for(int i=8; i > 0; i--){
            result.append(rowAsString(i, board));
        }
        result.append(whiteXAxis());
        return result.toString();
    }

    private String whiteXAxis(){
        return "    a" + EMPTY + " b" + EMPTY + " c" + EMPTY + " d" + EMPTY + " e"
                + EMPTY + " f" + EMPTY + " g" + EMPTY + " h" + EMPTY + "   \n";
    }

    private String blackXAxis(){
        return "    h" + EMPTY + " g" + EMPTY + " f" + EMPTY + " e" + EMPTY + " d"
                + EMPTY + " c" + EMPTY + " b" + EMPTY + " a" + EMPTY + "   \n";
    }

    private String rowAsString(int row, ChessBoard board){
        StringBuilder result = new StringBuilder();
        String label = BG_COLOR + COORDS_COLOR + " " + row + " ";
        result.append(label);
        for(int i = 1; i <= 8; i++){
            if ((row + i)%2 == 1){
                result.append(DARK_SQUARE);
            } else {
                result.append(LIGHT_SQUARE);
            }
            ChessPiece piece = board.getPiece(new ChessPosition(row, i));
            if (piece == null){
                result.append(EMPTY);
            } else {
                PieceType type = piece.getPieceType();
                ChessGame.TeamColor color = piece.getTeamColor();

                result.append(pieceAsString(type, color));
            }
        }
        result.append(label);
        result.append("\n");
        return result.toString();
    }

    private String pieceAsString(PieceType type, ChessGame.TeamColor color){
        if(color.equals(ChessGame.TeamColor.BLACK)){
            if(type.equals(PieceType.PAWN)){
                return BLACK_PAWN;
            } else if(type.equals(PieceType.ROOK)){
                return BLACK_ROOK;
            } else if(type.equals(PieceType.BISHOP)){
                return BLACK_BISHOP;
            } else if(type.equals(PieceType.KNIGHT)){
                return BLACK_KNIGHT;
            } else if(type.equals(PieceType.QUEEN)){
                return BLACK_QUEEN;
            } else if(type.equals(PieceType.KING)){
                return BLACK_KING;
            }
        } else if(color.equals(ChessGame.TeamColor.WHITE)){
            if(type.equals(PieceType.PAWN)){
                return WHITE_PAWN;
            } else if(type.equals(PieceType.ROOK)){
                return WHITE_ROOK;
            } else if(type.equals(PieceType.BISHOP)){
                return WHITE_BISHOP;
            } else if(type.equals(PieceType.KNIGHT)){
                return WHITE_KNIGHT;
            } else if(type.equals(PieceType.QUEEN)){
                return WHITE_QUEEN;
            } else if(type.equals(PieceType.KING)){
                return WHITE_KING;
            }
        }
        return EMPTY;
    }
}
