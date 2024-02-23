package service;

import model.*;
import server.Server;

public class UserService extends Service{
    public static Response login(LoginRequest request) {
        var userDAO = Server.getUserDAO();
        var username = request.username();
        var password = request.password();

        // check credentials
        if(!userDAO.verifyUser(username, password)){
            return new FailureResponse("Error: unauthorized");
        }

        var authDAO = Server.getAuthDAO();
        String authToken = authDAO.createAuth(generateAuth(), username);
        return new LoginResponse(username, authToken);

    }
}
