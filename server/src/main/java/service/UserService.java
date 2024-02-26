package service;

import dataAccess.DataAccessException;
import model.*;
import server.Server;

public class UserService extends Service{
    public static Response login(LoginRequest request) {
        var username = request.username();
        var password = request.password();

        // check credentials
        if(!userDAO.verifyUser(username, password)){
            return new FailureResponse("Error: unauthorized");
        }

        String authToken = authDAO.createAuth(generateAuth(), username);
        return new LoginResponse(username, authToken);
    }

    public static Response logout(LogoutRequest request) {
        var authToken = request.Authorization();

            if (authDAO.deleteAuth(authToken)) {
                return new LogoutResponse();
            } else {
                return new FailureResponse("Error: unauthorized");
            }

    }


}
