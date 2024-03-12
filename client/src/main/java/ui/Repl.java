package ui;

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

            if (input.equals("quit")) {
                System.out.println("bye");
                quit = true;
            } else if (input.equals("help")){
                System.out.println(HELP);
            } else if (input.equals("")) {
                System.out.println("wot?");

            }
        }

        scanner.close();
    }

}
