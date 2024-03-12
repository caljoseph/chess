import chess.*;
import ui.Repl;
import ui.ServerFacade;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter desired server:");
        var serverUrl = "http://localhost";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new Repl(new ServerFacade(serverUrl)).run();

    }
}