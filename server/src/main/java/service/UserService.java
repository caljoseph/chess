package service;

import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.response.FailureResponse;
import model.response.LoginResponse;
import model.response.LogoutResponse;
import model.response.Response;

public class UserService extends Service{

    //its good to handle the hashing sooner, perhaps here
    public static Response login(LoginRequest request) {
        var username = request.username();
        var password = request.password();

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        password = encoder.encode(password);

        // check credentials
        if(!userDAO.verifyUser(username, password)){
            return new FailureResponse("Error: unauthorized");
        }

        String authToken = authDAO.createAuth(generateAuth(), username);
        return new LoginResponse(username, authToken);
    }

    public static Response logout(LogoutRequest request) {
        var authToken = request.authorization();

            if (authDAO.deleteAuth(authToken)) {
                return new LogoutResponse();
            } else {
                return new FailureResponse("Error: unauthorized");
            }

    }


}
