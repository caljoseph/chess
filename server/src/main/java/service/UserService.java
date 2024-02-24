package service;

import dataAccess.DataAccessException;
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

    public static Response logout(LogoutRequest request) {
        var authDAO = Server.getAuthDAO();
        var authToken = request.Authorization();
        try {
            if (authDAO.deleteAuth(authToken)) {
                return new LogoutResponse();
            } else {
                return new FailureResponse("Error: unauthorized");
            }


        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
