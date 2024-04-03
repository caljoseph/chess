package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import ui.exception.ResponseException;
import com.google.gson.GsonBuilder;
import webSocketMessages.serverMessages.*;
import com.google.gson.JsonObject;


import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

import java.util.ArrayList;
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

    private final String HELP = """
            \u001B[0;35mredraw \u001B[0m- to show the gameboard
            \u001B[0;35mmark move \u001B[0m- to join a game
            \u001B[0;35mmoves\u001B[0m - highlight legal moves
            \u001B[0;35mresign \u001B[0;34m<ID>\u001B[0m - forfeit the game
            \u001B[0;35mleave\u001B[0m - exit gameplay for now
            \u001B[0;35mhelp\u001B[0m - show commands\u001B[0m
            """;
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
    }

    private void handleError(Error error) {
        System.out.println("Error: " + error.getErrorMessage());
    }

    private void handleLoadGame(LoadGame loadGame) {
        System.out.println("Game received: ");
        game = loadGame.getGame();
    }

    public void run() {
        if (playerColor != null) {
            System.out.println("joined game " + gameID);
            webSocketFacade.sendMessage(gson.toJson(new JoinPlayer(auth, Integer.valueOf(gameID))));
        } else {
            System.out.println("observing game " + gameID + " as an observer");

            webSocketFacade.sendMessage(gson.toJson(new JoinObserver(auth, Integer.valueOf(gameID))));
        }

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
                        drawBoard(true);
                    } else {
                        drawBoard(false);
                    }
                    break;
                case "leave":
                    webSocketFacade.sendMessage(gson.toJson(new Leave(auth, Integer.valueOf(gameID))));
                    System.out.println("\u001B[0mBye!");
                    return;
                case "move":
                    if (playerColor == null) {
                        System.out.println("\u001B[0mThis command is not available to observers");
                        break;
                    }
                    webSocketFacade.sendMessage(gson.toJson(new MakeMove(auth, new ChessMove(new ChessPosition(1,2), new ChessPosition(2,2), null), Integer.valueOf(gameID))));

                    System.out.println("\u001B[0mExample move");

                    break;
                case "resign":
                    if (playerColor == null) {
                        System.out.println("\u001B[0mThis command is not available to observers");
                        break;
                    }
                    webSocketFacade.sendMessage(gson.toJson(new Resign(auth, Integer.valueOf(gameID))));
                    System.out.println("\u001B[0mResign");
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


    private void drawBoard(boolean rightSideUp) {

        String[] topLetters = {"h", "g", "f", "e", "d", "c", "b", "a"};
        String[] sideNumbers = {"8", "7", "6", "5", "4", "3", "2", "1"};
        String[] edge = {"R", "N", "B", "K", "Q", "B", "N", "R"};

        int start = rightSideUp ? 8 : 1;
        int end = rightSideUp ? 1 : 8;
        int step = rightSideUp ? -1 : 1;

        if (!rightSideUp) {
            for (int i = 0; i < topLetters.length / 2; i++) {
                String temp = topLetters[i];
                topLetters[i] = topLetters[topLetters.length - 1 - i];
                topLetters[topLetters.length - 1 - i] = temp;
            }
        }

        System.out.print("\u001b[30;47;1m    ");
        for (String letter : topLetters) {
            System.out.print(letter + "  ");
        }
        System.out.println("  \u001B[49m");

        for (int i = start; rightSideUp ? i >= end : i <= end; i += step) {
            System.out.print("\u001b[30;47;1m " + sideNumbers[i - 1] + " ");
            if ( !rightSideUp ) {
                edge = new String[]{"R", "N", "B", "Q", "K", "B", "N", "R"};
            }

            for (int j = 0; j < 8; j++) {
                String piece = edge[j];
                boolean isWhitePiece = rightSideUp ? (i + j) % 2 == 0 : (i + j + 1) % 2 == 0;
                boolean isFirstRow = i == 8;
                boolean isSecondRow = i == 7;
                boolean isSeventhRow = i == 2;
                boolean isEighthRow = i == 1;

                if (isWhitePiece) {
                    if (isFirstRow || isSecondRow) {
                        System.out.print("\u001b[31;107m " + (isSecondRow ? "P" : piece) + " ");
                    } else if (isSeventhRow || isEighthRow) {
                        System.out.print("\u001b[34;107m " + (isSeventhRow ? "P" : piece) + " ");
                    } else {
                        System.out.print("\u001b[30;107m   ");
                    }
                } else {
                    if (isFirstRow || isSecondRow) {
                        System.out.print("\u001b[31;40m " + (isSecondRow ? "P" : piece) + " ");
                    } else if (isSeventhRow || isEighthRow) {
                        System.out.print("\u001b[34;40m " + (isSeventhRow ? "P" : piece) + " ");
                    } else {
                        System.out.print("\u001b[37;40m   ");
                    }
                }
            }
            System.out.println("\u001b[30;47;1m " + sideNumbers[i - 1] + " " + "\u001B[49m");
        }

        System.out.print("\u001b[30;47;1m    ");
        for (String letter : topLetters) {
            System.out.print(letter + "  ");
        }
        System.out.println("  \u001B[49m");
    }



}
