package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import ui.exception.ResponseException;

import java.io.InvalidClassException;
import java.util.*;

import ui.EscapeSequences;

public class Postlogin {
    private ServerFacade serverFacade;
    private final String username;
    private String auth;
    private HashMap<Integer, Integer> gameList = new HashMap<>();
    private ArrayList<GameData> gameDataList = new ArrayList<>();
    // maps display ID to true ID
    private int numGames = 0;
    private final String HELP = """
            \u001B[0;35mcreate \u001B[0;34m<NAME>\u001B[0m- to create a game
            \u001B[0;35mlist \u001B[0m- all games
            \u001B[0;35mjoin \u001B[0;34m<ID> [WHITE|BLACK|<empty>]\u001B[0m- to join a game
            \u001B[0;35mobserve \u001B[0;34m<ID>\u001B[0m- to observe a game
            \u001B[0;35mlogout\u001B[0m - when you are done
            \u001B[0;35mquit\u001B[0m - playing chess
            \u001B[0;35mhelp\u001B[0m - show commands\u001B[0m
            """;





    public Postlogin(ServerFacade facade, String username, String auth) {
        serverFacade = facade;
        this.username = username;
        this.auth = auth;
    }
    public void run() {
        System.out.println("Logged in as " + username);
        System.out.println("\u001B[32mGames:\u001B[0m");

        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        list(auth);

        while (!quit) {
            System.out.print("\u001B[32m[LOGGED IN]\u001B[0m  >>> "); // Print prompt in green
            String input = scanner.nextLine();
            var tokens = input.split(" ");

            switch (tokens[0]) {
                case "quit":
                    System.out.println("bye");
                    quit = true;
                    break;
                case "help":
                    System.out.println(HELP);
                    break;
                case "create":
                    if (tokens.length == 2) {
                        create(tokens[1]);
                    } else {
                        System.out.println("Invalid number of arguments for create game");
                    }
                    break;
                case "list":
                    list(auth);
                    break;
                case "join":
                    if (tokens.length != 3) {
                        System.out.println("Invalid number of arguments for join game");
                    }
                    else if (!isInteger(tokens[1]) || (!tokens[2].equals("WHITE") && !tokens[2].equals("BLACK"))) {
                        System.out.println("Invalid syntax, use: \u001B[0;35mjoin \u001B[0;34m<ID> [WHITE|BLACK|<empty>]\u001B[0m to join a game");
                    } else {
                        if(join(tokens[1], tokens[2])) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            list(auth);
                        }
                    }
                    break;
                case "observe":
                    if (tokens.length != 2) {
                        System.out.println("Invalid number of arguments for observe");
                    } else if (!isInteger(tokens[1])){
                        System.out.println("Invalid syntax, use: \u001B[0;35mobserve \u001B[0;34m<ID> \u001B[0m to observe a game");
                    } else {
                        if(join(tokens[1], null)) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            list(auth);
                        }
                    }
                    break;
                case "logout":
                    logout(auth);
                    return;
                default:
                    System.out.println("invalid input");
                    break;
            }
        }
        scanner.close();
    }

    void logout(String auth) {
        try {
            serverFacade.logout(auth);
        } catch (ResponseException e){
            System.out.println("Logout failed: " + e);
        }
    }
    void list(String auth) {
        try {
            var res = serverFacade.listGames(auth);
            numGames = 1;
            for (GameData game : res.games()) {

                String whiteUsername = (game.whiteUsername() != null ? game.whiteUsername() : "EMPTY");
                String blackUsername = (game.blackUsername() != null ? game.blackUsername() : "EMPTY");

                System.out.println("\u001B[0m" + numGames + ": " + game.gameName() + " - \u001B[0;35mWhite: \u001B[0;34m" + whiteUsername + " \u001B[0;35mBlack: \u001B[0;34m" + blackUsername);
                gameList.put(numGames, game.gameID());

                var gameData = new GameData(numGames, whiteUsername, blackUsername, game.gameName(), null);
                gameDataList.add(gameData);

                numGames += 1;
            }
        } catch (ResponseException e) {
            System.out.println("List games failed: " + e);
        }
    }
    boolean join(String gameID, String playerColor) {
        String dbID = String.valueOf(gameList.get(Integer.valueOf(gameID)));
        try {
            if (!gameList.containsKey(Integer.valueOf(gameID))) { throw new ResponseException(1000, "This game does not exist."); }
            if (playerColor == null) {
                serverFacade.joinGame(auth, null, dbID);
                new Gameplay(serverFacade, username, auth, null, dbID).run();
                return true;
            }
            for (GameData game : gameDataList) {
                if (game.gameID() != Integer.valueOf(gameID)) { continue; }
                if (game.whiteUsername().equals(username) && playerColor.equals("WHITE") ||
                        game.blackUsername().equals(username) && playerColor.equals("BLACK")) {

                    new Gameplay(serverFacade, username, auth, playerColor, dbID).run();
                    return true;
                }
                if (game.whiteUsername().equals(username) && playerColor.equals("BLACK") ||
                        game.blackUsername().equals(username) && playerColor.equals("WHITE")){
                    throw new ResponseException(1000, "You already joined this game as the other color.");
                }
            }

            serverFacade.joinGame(auth, playerColor, dbID);
            new Gameplay(serverFacade, username, auth, playerColor, dbID).run();
            return true;

        } catch (ResponseException e) {
            if (e.getMessage().equals("failure: 403")) {
                System.out.println("The spot is already taken.");
                return false;
            } else if (e.getMessage().equals("You already joined this game as the other color.")) {
                System.out.println(e.getMessage());
                return false;
            } else if (e.getMessage().equals("This game does not exist.")) {
                System.out.println(e.getMessage());
                return false;
            } else {
                System.out.println("Join game failed with message " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    void create(String gameName) {
        try {
            var newGame = serverFacade.createGame(auth, gameName);
            list(auth);
        } catch (ResponseException e) {
            System.out.println("Create game failed: " + e);
        }
    }
    private void printCorrespondingPairs() {
        for (Map.Entry<Integer, Integer> entry : gameList.entrySet()) {
            System.out.println("Display ID: " + entry.getKey() + ", True ID: " + entry.getValue());
        }
    }
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
