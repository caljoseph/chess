package ui;

import model.request.LoginRequest;
import ui.exception.ResponseException;

import java.util.Scanner;


public class Repl {

    private ServerFacade serverFacade;
    public Repl (ServerFacade facade) {
        this.serverFacade = facade;
    }

    public void run() {
        new Prelogin(serverFacade).run();
    }






}
