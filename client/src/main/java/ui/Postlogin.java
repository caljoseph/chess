package ui;

import model.GameData;
import ui.exception.ResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Postlogin {
    private ServerFacade serverFacade;
    private String username;
    private String auth;
    private HashMap<Integer, Integer> gameList = new HashMap<>();
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

        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            System.out.print("\u001B[32m[LOGGED IN]\u001B[0m  >>> "); // Print prompt in green
            String input = scanner.nextLine();
            var tokens = input.split(" ");

            switch (tokens[0]) {
                case "quit":
                    System.out.println("bye");
                    quit = true;
                    // this should maybe log me out?
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
                        System.out.println("Invalid number of arguments for create game");
                    }
                    else if (!isInteger(tokens[1]) || (!tokens[2].equals("WHITE") && !tokens[2].equals("BLACK"))) {
                        System.out.println("Invalid syntax, use: \u001B[0;35mjoin \u001B[0;34m<ID> [WHITE|BLACK|<empty>]\u001B[0m to join a game");
                    } else {
                        if (join(tokens[1], tokens[2])) {
                            new Gameplay(serverFacade, username).run();
                        }
                    }
                    break;
                case "observe":
                    if (tokens.length != 2) {
                        System.out.println("Invalid number of arguments for observe");
                    } else {
                        new Gameplay(serverFacade, username).run();
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
                System.out.println(numGames + ": " + game.gameName());
                gameList.put(numGames, game.gameID());
                numGames += 1;
            }
        } catch (ResponseException e) {
            System.out.println("List games failed: " + e);
        }
    }
    boolean join(String gameID, String playerColor) {
        try {
            var joinedGame = serverFacade.joinGame(auth, playerColor, String.valueOf(gameList.get(Integer.valueOf(gameID))));
            System.out.println("joined game " + gameID);
            return true;

        } catch (ResponseException e) {
            if (e.getMessage().equals("failure: 403")) {
                System.out.println("The spot is already taken.");
                return false;
            } else {
                System.out.println("Join game failed with message " + e.getMessage());
                return false;
            }
        }
    }
    void create(String gameName) {
        try {
            var newGame = serverFacade.createGame(auth, gameName);
            System.out.println("Created game with true ID: " + newGame.gameID());
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
