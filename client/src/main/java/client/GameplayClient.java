package client;

import chess.*;
import chess.ChessPiece.*;
import repl.Repl;
import results.Game;
import ui.ServerFacade;
import websocket.WebSocketFacade;

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

    public GameplayClient(String serverURL, Repl repl, String authToken, String playerColor, Game game) throws Exception{
        facade = new ServerFacade(serverURL);
        this.authToken = authToken;
        this.repl = repl;
        this.serverURL = serverURL;
        this.playerColor = playerColor;
        this.game = game;

        ws = new WebSocketFacade(this.serverURL, this.repl);
        ws.connect(this.authToken, this.game.gameID());
    }

    public String eval(String line) throws Exception {
        var tokens = line.toLowerCase().split(" ");
        var command = (tokens.length > 0) ? tokens[0] : "help";

        if (command.equals("help")){
            return help();
        } else if (command.equals("show_board")) {
            ChessBoard startingBoard = new ChessBoard();
            startingBoard.resetBoard();
            return showBoard(startingBoard);
        } else if (command.equals("change_colors")){
            return changeColors(tokens);
        } else if (command.equals("exit_game")) {
            return exitGame();
        }else if (command.equals("resign")){
            //return resign();
        } else if (command.equals("make_move")){
            return makeMove(tokens);
        } else if (command.equals("quit")){
            return "quit";
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
                    options: greyscale (default), vibrant.
                exit_game - exits the game
                resign - forfeit the match
                make_move <start> <end> - move a piece if it is your turn
                quit - quit the program
                
                More commands to come once gameplay is implemented!
                \\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//o\\o//
                """;
    }

    public String makeMove(String[] tokens) throws Exception {
        if(tokens.length != 3){
            return "Usage: make_move <start> <end>";
        }
        ChessPosition start = parseCoords(tokens[1]);
        ChessPosition end = parseCoords(tokens[2]);
        if (start == null || end == null){
            return "Coords should be letter-number pairs, i.e a7 b5";
        }
        //check if move is valid
        ChessMove move = new ChessMove(start, end, null); //need to add pawn promotion feature
        ws = new WebSocketFacade(this.serverURL, this.repl);
        ws.makeMove(this.authToken, this.game.gameID(), move);
        return "Made move successfully";
    }

    private ChessPosition parseCoords(String coord){
        if (coord.length() != 2){
            return null;
        }
        try{
            int row = Integer.parseInt(coord[1]);//figure out syntax for this
            int col;
            char colLetter = coord[0];
            if (colLetter == 'a'){
                col = 1;
            } else if (colLetter == 'b'){
                col = 2;
            } else if (colLetter == 'c'){
                col = 3;
            } else if (colLetter == 'd'){
                col = 4;
            } else if (colLetter == 'e'){
                col = 5;
            } else if (colLetter == 'f'){
                col = 6;
            } else if (colLetter == 'g'){
                col = 7;
            } else if (colLetter == 'h'){
                col = 8;
            } else {
                return null;
            }
            return new ChessPosition(row, col);
        } catch (NumberFormatException ex){
            return null;
        }
    }

    public String exitGame() throws Exception{
        repl.client = new LoggedInClient(serverURL, repl, authToken);
        return "Successfully exited game " + game.gameName();
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
        } else {
            return "Color scheme not recognized. Options: greyscale, vibrant";
        }
        return "Color scheme of board successfully changed to " + colorScheme;
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
