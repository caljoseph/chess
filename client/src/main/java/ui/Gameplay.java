package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import ui.exception.PositionParseException;
import ui.exception.ResponseException;
import com.google.gson.GsonBuilder;
import webSocketMessages.serverMessages.*;
import com.google.gson.JsonObject;


import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// The big to do here is to make a better function that prints out the current board.

public class Gameplay implements OnMessageReceivedListener{
    private ServerFacade serverFacade;
    private WebSocketFacade webSocketFacade;
    private ChessGame game;
    private final String username;
    private final String auth;
    private final String gameID;
    private final String playerColor;

    private final static String HELP = """
            \u001B[0;35mredraw \u001B[0m- to show the gameboard
            \u001B[0;35mmark move \u001B[0m- to join a game
            \u001B[0;35mmoves\u001B[0m - highlight legal moves
            \u001B[0;35mresign \u001B[0;34m<ID>\u001B[0m - forfeit the game
            \u001B[0;35mleave\u001B[0m - exit gameplay for now
            \u001B[0;35mhelp\u001B[0m - show commands\u001B[0m
            """;
    Map<Character, Integer> letterToNumber = new HashMap<>();



    Gson gson = new Gson();
    public Gameplay(ServerFacade facade, String username, String auth, String playerColor, String gameID) throws ResponseException {
        serverFacade = facade;
        this.webSocketFacade =  serverFacade.initiateWebSocket(this);;
        this.username = username;
        this.auth = auth;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
    @Override
    public void onMessageReceived(String message) {
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

        String typeStr = jsonObject.get("serverMessageType").getAsString();
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.valueOf(typeStr);

        ServerMessage serverMessage;

        switch (type) {
            case LOAD_GAME:
                handleLoadGame(gson.fromJson(message, LoadGame.class));
                break;
            case ERROR:
                handleError(gson.fromJson(message, Error.class));
                break;
            case NOTIFICATION:
                handleNotification(gson.fromJson(message, Notification.class));
                break;
            default:
                throw new IllegalArgumentException("Unhandled server message type: " + type);
        }
    }

    private void handleNotification(Notification notification) {
        System.out.println(notification.getMessage());
        System.out.print("\u001B[49m\u001B[32m[PLAYING]\u001B[0m  >>> ");
    }

    private void handleError(Error error) {
        System.out.println("Error: " + error.getErrorMessage());
    }

    private void handleLoadGame(LoadGame loadGame) {
        game = loadGame.getGame();
        if (playerColor == null || playerColor.equals("WHITE")) {
            drawBoard(ChessGame.TeamColor.WHITE);
        } else {
            drawBoard(ChessGame.TeamColor.BLACK);
        }
        System.out.print("\n\u001B[49m\u001B[32m[PLAYING]\u001B[0m  >>> ");
    }

    public void run() {
        if (playerColor != null) {
            System.out.println("joined game " + gameID);
            webSocketFacade.sendMessage(gson.toJson(new JoinPlayer(auth, Integer.valueOf(gameID), ChessGame.TeamColor.valueOf(playerColor))));
        } else {
            System.out.println("observing game " + gameID + " as an observer");

            webSocketFacade.sendMessage(gson.toJson(new JoinObserver(auth, Integer.valueOf(gameID))));
        }

        createLetterMapping();
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            System.out.print("\u001B[49m\u001B[32m[PLAYING]\u001B[0m  >>> ");
            String input = scanner.nextLine();
            var tokens = input.split(" ");

            switch (tokens[0]) {
                case "quit":
                    System.out.println("\u001B[0mbye");
                    quit = true;
                    break;
                case "help":
                    System.out.println(HELP);
                    break;
                case "redraw":
                    if (playerColor == null || playerColor.equals("WHITE")) {
                        drawBoard(ChessGame.TeamColor.WHITE);
                    } else {
                        drawBoard(ChessGame.TeamColor.BLACK);
                    }
                    break;
                case "leave":
                    if (playerColor == null){
                        webSocketFacade.sendMessage(gson.toJson(new Leave(auth, Integer.valueOf(gameID), null)));
                    }
                    else if (playerColor.equals("WHITE")){
                        webSocketFacade.sendMessage(gson.toJson(new Leave(auth, Integer.valueOf(gameID), ChessGame.TeamColor.WHITE)));
                    } else {
                        webSocketFacade.sendMessage(gson.toJson(new Leave(auth, Integer.valueOf(gameID), ChessGame.TeamColor.BLACK)));
                    }
                    System.out.println("\u001B[0mBye!");
                    return;
                case "move":
                    if (playerColor == null) {
                        System.out.println("\u001B[0mThis command is not available to observers");
                        break;
                    }
                    if (tokens.length != 3) {
                        System.out.println("\u001B[0mIncorrect number of arguments for making a move");
                        break;
                    }

                    try {
                        int startRow = extractCoordinates(tokens[1])[0];
                        int startCol = extractCoordinates(tokens[1])[1];
                        int endRow = extractCoordinates(tokens[2])[0];
                        int endCol = extractCoordinates(tokens[2])[1];

                        webSocketFacade.sendMessage(gson.toJson(new MakeMove(auth, new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(endRow,endCol), null), Integer.valueOf(gameID))));
                    } catch (PositionParseException e) {
                        System.out.println("\u001B[0m" + e.getMessage());
                    }
                    break;
                case "resign":
                    if (playerColor == null) {
                        System.out.println("\u001B[0mThis command is not available to observers");
                        break;
                    }
                    webSocketFacade.sendMessage(gson.toJson(new Resign(auth, Integer.valueOf(gameID))));
                    break;
                case "highlight legal moves":
                    System.out.println("\u001B[0mTO DO");

                    break;

                default:
                    System.out.println("\u001B[0minvalid input");
                    break;
            }
        }

    }
    private int[] extractCoordinates(String position) throws PositionParseException {
        if (position == null || position.length() != 2) {
            throw new PositionParseException("Invalid position format. Position must be exactly 2 characters.");
        }

        char rowChar = position.charAt(0);
        if (rowChar < '1' || rowChar > '8') {
            throw new PositionParseException("Invalid row. Must be a digit from 1 to 8.");
        }
        int row = Character.getNumericValue(rowChar);

        char columnLetter = position.charAt(1);
        if (!letterToNumber.containsKey(columnLetter)) {
            throw new PositionParseException("Invalid column. Must be a letter from 'a' to 'h'.");
        }
        int column = letterToNumber.get(columnLetter);

        return new int[]{row, column};
    }


    //RED IS WHITE
    //UPPERCASE IS WHITE

    //BLUE IS BLACK
    //LOWERCASE IS BLACK

    private void drawBoard(ChessGame.TeamColor color) {
        var board = game.getBoard();
        System.out.println(board.toString());

        boolean white = color == ChessGame.TeamColor.WHITE;
        int startI = white ? 8 : 1;
        int stepI = white ? -1 : 1;
        int startJ = white ? 1 : 8;
        int stepJ = white ? 1 : -1;

        String[] topLetters;
        if (white) {
            topLetters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        } else {
            topLetters = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
        }
        String[] sideNumbers = {"1", "2", "3", "4", "5", "6", "7", "8"};

        System.out.print("\u001b[30;47;1m    ");
        for (String letter : topLetters) {
            System.out.print(letter + "  ");
        }
        System.out.println("  \u001B[49m");

        for (int i = startI; white ? i > 0 : i < 9; i += stepI) {
            System.out.print("\u001b[30;47;1m " + sideNumbers[white ? i - 1 : i - 1] + " ");

            for (int j = startJ; white ? j < 9 : j > 0; j += stepJ) {
                boolean isWhiteSquare = (i + j + 1) % 2 == 0;

                ChessPosition position = new ChessPosition(i,j);

                ChessPiece piece = board.getPiece(position);
                if (piece == null) {
                    if (isWhiteSquare) {
                        System.out.print("\u001b[34;107m   ");
                    } else {
                        System.out.print("\u001b[31;40m   ");
                    }
                    continue;
                }

                if (isWhiteSquare) {
                    if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                        System.out.print("\u001b[31;107m " + piece.getPieceType().getLetter() + " ");
                    } else {
                        System.out.print("\u001b[34;107m " + piece.getPieceType().getLetter() + " ");
                    }
                } else {
                    if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                        System.out.print("\u001b[31;40m " + piece.getPieceType().getLetter() + " ");
                    } else {
                        System.out.print("\u001b[34;40m " + piece.getPieceType().getLetter() + " ");
                    }
                }
            }
            System.out.println("\u001b[30;47;1m " + sideNumbers[white ? i - 1 : i - 1] + " " + "\u001B[49m");
        }

        System.out.print("\u001b[30;47;1m    ");
        for (String letter : topLetters) {
            System.out.print(letter + "  ");
        }
        System.out.println("  \u001B[49m");
    }
    void createLetterMapping() {
        letterToNumber.put('a', 1);
        letterToNumber.put('b', 2);
        letterToNumber.put('c', 3);
        letterToNumber.put('d', 4);
        letterToNumber.put('e', 5);
        letterToNumber.put('f', 6);
        letterToNumber.put('g', 7);
        letterToNumber.put('h', 8);
    }
}
