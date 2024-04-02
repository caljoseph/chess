package ui;

import ui.exception.ResponseException;

import java.util.Scanner;


public class Prelogin {
    private ServerFacade serverFacade;
    private final String GREETING = "Welcome to 240 Chess. Type 'help' to get started.";
    private final String HELP = "\u001B[0;35m" +
            "register \u001B[0;34m<USERNAME> <PASSWORD> <EMAIL> \u001B[0m- to create an account\n" +
            "\u001B[0;35mlogin \u001B[0;34m<USERNAME> <PASSWORD> \u001B[0m- to play chess\n" +
            "\u001B[0;35mquit\u001B[0m - playing chess\n" +
            "\u001B[0;35mhelp\u001B[0m - show commands\u001B[0m";

    public Prelogin(ServerFacade facade) { serverFacade = facade; }
    public void run() {
        System.out.println(GREETING);


        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            System.out.print("\u001B[31m[LOGGED OUT]\u001B[0m  >>> \u001B[0m");
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
                case "login":
                    if (tokens.length == 3) {
                        login(tokens[1], tokens[2]);
                    } else {
                        System.out.println("Invalid number of arguments for login");
                    }
                    break;
                case "register":
                    if (tokens.length == 4) {
                        register(tokens[1], tokens[2], tokens[3]);
                    } else {
                        System.out.println("Invalid number of arguments for register");
                    }
                    break;
                default:
                    System.out.println("invalid input");
                    break;
            }
        }
        scanner.close();
    }

    private void login(String username, String password) {
        try {
            var res = serverFacade.login(username, password);
            String auth = res.authToken;

            new Postlogin(serverFacade, username, auth).run();
        } catch (ResponseException e) {
            if (e.getMessage().equals("failure: 401")) {
                System.out.println("Incorrect credentials");
            } else {
                System.out.println("Login failed: " + e);
            }

        }
    }

    private void register(String username, String password, String email) {
        try {
            var res = serverFacade.register(username, password, email);
            String auth = res.authToken;

            new Postlogin(serverFacade, username, auth).run();
        } catch (ResponseException e) {
            if (e.getMessage().equals("failure: 403")) {
                System.out.println("Username taken");
            } else {
                System.out.println("Registration failed: " + e);
            }
        }
    }
}
