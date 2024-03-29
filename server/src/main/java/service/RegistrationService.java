package service;

import model.*;
import model.request.RegisterRequest;
import model.response.FailureResponse;
import model.response.RegisterResponse;
import model.response.Response;

public class RegistrationService extends Service {


    public static Response register(RegisterRequest request){
        // Let's check if it's a bad request
        if (!isValid(request)) {
            return new FailureResponse("Error: bad request");
        }

        // Let's check if this user is taken
        if (userDAO.getUser(request.username()) != null) {
            return new FailureResponse("Error: already taken");
        }

        // encode password
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        var password = encoder.encode(request.password());

        // new user, lets add them
        var newUser = new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(newUser);

        // let's get them an authToken
        var auth = generateAuth();
        auth = authDAO.createAuth(auth, newUser.username());

        return new RegisterResponse(newUser.username(), auth);
    }

    private static boolean isValid(RegisterRequest request){
        return request.username() != null &&
                request.email() != null &&
                request.password() != null;
    }



}

