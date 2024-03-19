package ui;

import model.request.LoginRequest;
import ui.exception.ResponseException;

import java.util.Scanner;


public class Repl {
    private final String GREETING = "Welcome to 240 Chess. Type 'help' to get started.";
    private final String HELP = """
            register <USERNAME> <PASSWORD> <EMAIL> - to create an account
            login <USERNAME> <PASSWORD> - to play chess
            quit - playing chess
            help - with possible commands
            """;
    private ServerFacade serverFacade;
    public Repl (ServerFacade facade) {
        this.serverFacade = facade;
    }

    public void run() {
        System.out.println(GREETING);

        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {

            String input = scanner.nextLine();
            var tokens = input.split(" ");

            if (tokens[0].equals("quit")) {
                System.out.println("bye");
                quit = true;
            } else if (tokens[0].equals("help")){
                System.out.println(HELP);
            } else if (tokens[0].equals("login") && tokens.length == 3) {
                    if(login(tokens[1], tokens[2])) {
                        System.out.println("Successful login");
                    } else {
                        System.out.println("Failed login");
                    }
            } else if (tokens[0].equals("register") && tokens.length == 4) {
                if(register(tokens[1], tokens[2], tokens[3])) {
                    System.out.println("Successful register");
                } else {
                    System.out.println("Failed register");
                }
            } else {
                System.out.println("invalid input");
            }
        }
        scanner.close();
    }


    private boolean login(String username, String password) {

        try {
            serverFacade.login(username, password);
            return true;
        } catch (ResponseException e) {
            return false;
        }

    }
    private boolean register(String username, String password, String email) {
        try {
            serverFacade.register(username, password, email);
            return true;
        } catch (ResponseException e) {
            return false;
        }

    }



}
