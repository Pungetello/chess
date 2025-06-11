package client;

import chess.*;
import chess.ChessPiece.*;
import repl.Repl;
import results.Game;
import ui.ServerFacade;
import websocket.WebSocketFacade;
import websocket.messages.NotificationMessage;

import static ui.EscapeSequences.*;

public class GameplayClient extends Client {
    ServerFacade facade;
    String authToken;
    String serverURL;
    Repl repl;
    String playerColor;
    Game game;
    WebSocketFacade ws;
    String bgColor = RESET_BG_COLOR;
    String darkSquare = SET_BG_COLOR_DARK_GREY;
    String lightSquare = SET_BG_COLOR_LIGHT_GREY;
    String coordsColor = RESET_TEXT_COLOR;
    String whiteColor = SET_TEXT_COLOR_WHITE;
    String blackColor = SET_TEXT_COLOR_BLACK;
    ChessBoard board = new ChessBoard();

    public GameplayClient(String serverURL, Repl repl, String authToken, String playerColor, Game game) throws Exception{
        facade = new ServerFacade(serverURL);
        this.authToken = authToken;
        this.repl = repl;
        this.serverURL = serverURL;
        this.playerColor = playerColor;
        this.game = game;

        ws = new WebSocketFacade(this, this.serverURL, this.repl);
        ws.connect(this.authToken, this.game.gameID());
        //showBoard(board);
    }

    public String eval(String line) throws Exception {
        var tokens = line.toLowerCase().split(" ");
        var command = (tokens.length > 0) ? tokens[0] : "help";

        if (command.equals("help")){
            return help();
        } else if (command.equals("show_board")) {
            return showBoard(board);
        } else if (command.equals("change_colors")){
            return changeColors(tokens);
        } else if (command.equals("leave_game")) {
            return leaveGame();
        }else if (command.equals("resign")){
            return resign();
        } else if (command.equals("make_move")){
            return makeMove(tokens);
        } else {
            return "Command not recognized. Type 'help' for list of commands.";
        }
    }

    public String help() {
        return """
                
                //o\\o//o\\o//o\\o//o\\COMMANDS//o\\o//o\\o//o\\o//o\\
                help - see a list of commands
                show_board - display the current chessboard
                change_colors <color scheme> - change the color scheme of the board.
                    options: greyscale (default), vibrant, blues.
                leave_game - exits the game
                resign - forfeit the match
                make_move <start> <end> <promotion piece>(optional) - move a piece
                
                More commands to come once gameplay is implemented!
                \\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//
                """;
    }

    public String makeMove(String[] tokens) throws Exception {
        if(tokens.length != 3 && tokens.length != 4){
            return "Usage: make_move <start> <end> <promotion piece>(if needed)";
        }
        ChessPosition start = parseCoords(tokens[1]);
        ChessPosition end = parseCoords(tokens[2]);
        ChessPiece.PieceType promotionPiece = null;
        if(tokens.length == 4){
            try {
                promotionPiece = parsePromotion(tokens[3].toUpperCase());
            } catch (Exception ex){
                return "Promotion piece should be: rook, bishop, knight, or queen. If not promoting a pawn, leave blank.";
            }
        }
        if (start == null || end == null){
            return "Coords should be letter-number pairs, i.e a7 b5";
        }
        ChessMove move = new ChessMove(start, end, promotionPiece);
        ws = new WebSocketFacade(this, this.serverURL, this.repl);
        ws.makeMove(this.authToken, this.game.gameID(), move);
        return "Making move";
    }

    private ChessPosition parseCoords(String coord){
        if (coord.length() != 2){
            return null;
        }
            char rowChar = coord.charAt(1);
            if (Character.isDigit(rowChar)) {
                int row = rowChar - '0';
                int col;
                char colLetter = coord.charAt(0);
                if (colLetter == 'a') {
                    col = 1;
                } else if (colLetter == 'b') {
                    col = 2;
                } else if (colLetter == 'c') {
                    col = 3;
                } else if (colLetter == 'd') {
                    col = 4;
                } else if (colLetter == 'e') {
                    col = 5;
                } else if (colLetter == 'f') {
                    col = 6;
                } else if (colLetter == 'g') {
                    col = 7;
                } else if (colLetter == 'h') {
                    col = 8;
                } else {
                    return null;
                }
                return new ChessPosition(row, col);
            } else {
                return null;
            }
    }

    private ChessPiece.PieceType parsePromotion(String promotionString) throws Exception{
        ChessPiece.PieceType result;
        if(promotionString.equals("ROOK")){
            result = ChessPiece.PieceType.ROOK;
        } else if(promotionString.equals("BISHOP")){
            result = ChessPiece.PieceType.BISHOP;
        } else if(promotionString.equals("KNIGHT")){
            result = ChessPiece.PieceType.KNIGHT;
        } else if(promotionString.equals("QUEEN")){
            result = ChessPiece.PieceType.QUEEN;
        } else {
            throw new Exception("invalid promotion piece");
        }
        return result;
    }

    public void setBoard(ChessBoard board){
        this.board = board;
    }

    public String leaveGame() throws Exception{
        ws = new WebSocketFacade(this, this.serverURL, this.repl);
        ws.leave(this.authToken, this.game.gameID());

        repl.client = new LoggedInClient(serverURL, repl, authToken); // update for websocket
        return "Leaving " + game.gameName();
    }

    public String resign() throws Exception{
        ws = new WebSocketFacade(this, this.serverURL, this.repl);
        ws.resign(this.authToken, this.game.gameID());

        repl.client = new LoggedInClient(serverURL, repl, authToken);
        return "Resigning from " + this.game.gameName();
    }

    public String changeColors(String[] tokens){
        if(tokens.length != 2){
            return "Usage: change_colors <color scheme>";
        }
        String colorScheme = tokens[1];
        if(colorScheme.equals("greyscale")){
            bgColor = RESET_BG_COLOR;
            darkSquare = SET_BG_COLOR_DARK_GREY;
            lightSquare = SET_BG_COLOR_LIGHT_GREY;
            coordsColor = RESET_TEXT_COLOR;
            whiteColor = SET_TEXT_COLOR_WHITE;
            blackColor = SET_TEXT_COLOR_BLACK;
        } else if(colorScheme.equals("vibrant")){
            bgColor = RESET_BG_COLOR;
            darkSquare = SET_BG_COLOR_CYAN;
            lightSquare = SET_BG_COLOR_MAGENTA;
            coordsColor = SET_TEXT_COLOR_YELLOW;
            whiteColor = SET_TEXT_COLOR_RED;
            blackColor = SET_TEXT_COLOR_BLUE;
        } else if(colorScheme.equals("blues")){
            bgColor = RESET_BG_COLOR;
            darkSquare = SET_BG_COLOR_TEAL;
            lightSquare = SET_BG_COLOR_CYAN;
            coordsColor = SET_TEXT_COLOR_BLUE;
            whiteColor = SET_TEXT_COLOR_WHITE;
            blackColor = SET_TEXT_COLOR_BLUE;
        } else {
            return "Color scheme not recognized. Options: greyscale, vibrant";
        }
        return "Color scheme of board successfully changed to " + colorScheme;
    }

    public void showBoard(ChessBoard board) {
        String prettyBoard;
        if (playerColor.equals("BLACK")){
            prettyBoard = printBlackBoard(board);
        } else {
            prettyBoard = printWhiteBoard(board);
        }
        repl.notify(new NotificationMessage(prettyBoard));
    }

    private String printBlackBoard(ChessBoard board){
        StringBuilder result = new StringBuilder();
        result.append(bgColor)
                .append(coordsColor)
                .append(blackXAxis());
        for(int i=1; i <= 8; i++){
            result.append(rowAsString(i, board, ChessGame.TeamColor.BLACK));
        }
        result.append(blackXAxis())
                .append(RESET_BG_COLOR)
                .append(RESET_TEXT_COLOR);
        return result.toString();
    }

    private String printWhiteBoard(ChessBoard board){
        StringBuilder result = new StringBuilder();
        result.append(bgColor)
                .append(coordsColor)
                .append(whiteXAxis());
        for(int i=8; i > 0; i--){
            result.append(rowAsString(i, board, ChessGame.TeamColor.WHITE));
        }
        result.append(whiteXAxis())
                .append(RESET_BG_COLOR)
                .append(RESET_TEXT_COLOR);
        return result.toString();
    }

    private String whiteXAxis(){
        return "    a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h\u2003   \n";
    }

    private String blackXAxis(){
        return "    h\u2003 g\u2003 f\u2003 e\u2003 d\u2003 c\u2003 b\u2003 a\u2003   \n";
    }

    private String rowAsString(int row, ChessBoard board, ChessGame.TeamColor playerColor){
        StringBuilder result = new StringBuilder();
        String label = bgColor + coordsColor + " " + row + " ";
        result.append(label);
        if(playerColor.equals(ChessGame.TeamColor.WHITE)) {
            for (int i = 1; i <= 8; i++) {
                result.append(squareAsString(row, i, board));
            }
        } else {
            for (int i = 8; i >= 1; i--) {
                result.append(squareAsString(row, i, board));
            }
        }
        result.append(label);
        result.append("\n");
        return result.toString();
    }

    private String squareAsString(int row, int col, ChessBoard board){
        StringBuilder result = new StringBuilder();
        if ((row + col) % 2 == 0) {
            result.append(darkSquare);
        } else {
            result.append(lightSquare);
        }
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            result.append(EMPTY);
        } else {
            PieceType type = piece.getPieceType();
            ChessGame.TeamColor color = piece.getTeamColor();

            result.append(pieceAsString(type, color));
        }
        return result.toString();
    }

    private String pieceAsString(PieceType type, ChessGame.TeamColor color){
        if(color.equals(ChessGame.TeamColor.BLACK)){
            if(type.equals(PieceType.PAWN)){
                return blackColor + BLACK_PAWN;
            } else if(type.equals(PieceType.ROOK)){
                return blackColor + BLACK_ROOK;
            } else if(type.equals(PieceType.BISHOP)){
                return blackColor + BLACK_BISHOP;
            } else if(type.equals(PieceType.KNIGHT)){
                return blackColor + BLACK_KNIGHT;
            } else if(type.equals(PieceType.QUEEN)){
                return blackColor + BLACK_QUEEN;
            } else if(type.equals(PieceType.KING)){
                return blackColor + BLACK_KING;
            }
        } else if(color.equals(ChessGame.TeamColor.WHITE)){
            if(type.equals(PieceType.PAWN)){
                return whiteColor + WHITE_PAWN;
            } else if(type.equals(PieceType.ROOK)){
                return whiteColor + WHITE_ROOK;
            } else if(type.equals(PieceType.BISHOP)){
                return whiteColor + WHITE_BISHOP;
            } else if(type.equals(PieceType.KNIGHT)){
                return whiteColor + WHITE_KNIGHT;
            } else if(type.equals(PieceType.QUEEN)){
                return whiteColor + WHITE_QUEEN;
            } else if(type.equals(PieceType.KING)){
                return whiteColor + WHITE_KING;
            }
        }
        return EMPTY;
    }


}
