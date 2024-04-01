package ui;

import java.util.ArrayList;
import java.util.Scanner;

public class Gameplay {
    private ServerFacade serverFacade;
    private final String username;
    private final String HELP = """
            \u001B[0;35mredraw chess board \u001B[0;34m<NAME>\u001B[0m- to create a game
            \u001B[0;35mmark move \u001B[0;34m<ID> [WHITE|BLACK|<empty>]\u001B[0m- to join a game
            \u001B[0;35mhighlight legal moves\u001B[0m - when you are done
            \u001B[0;35mresign \u001B[0;34m<ID>\u001B[0m- to observe a game
            \u001B[0;35mleave\u001B[0m- all games
            \u001B[0;35mhelp\u001B[0m - show commands\u001B[0m
            """;
    public Gameplay(ServerFacade facade, String username) {
        serverFacade = facade;
        this.username = username;
    }
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        drawBoard(true);
        System.out.println("");
        drawBoard(false);

        while (!quit) {
            System.out.print("\u001B[49m\u001B[32m[PLAYING]\u001B[0m  >>> ");
            String input = scanner.nextLine();
            var tokens = input.split(" ");


            switch (tokens[0]) {
                case "quit":
                    System.out.println("\u001B[0mbye");
                    quit = true;
                    // this should maybe log me out?
                    break;
                case "help":
                    System.out.println(HELP);
                    break;
                case "redraw chess board":
                    System.out.println("\u001B[0mTO DO");

                    break;
                case "leave":
                    System.out.println("\u001B[0mTO DO");

                    break;
                case "mark move":
                    System.out.println("\u001B[0mTO DO");

                    break;
                case "resign":
                    System.out.println("\u001B[0mTO DO");

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
