package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import server.request.LoginRequest;
import server.request.LogoutRequest;
import server.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import server.response.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @BeforeEach
    void setUp() {
        var userDAO = new MemoryUserDAO();
        userDAO.clear();
        var authDAO = new MemoryAuthDAO();
        authDAO.clear();
    }

    @Test
    void testSuccessfulLogin() {
        RegistrationService.register(new RegisterRequest("validUser", "validPassword", ""));
        LoginRequest request = new LoginRequest("validUser", "validPassword");

        Response response = UserService.login(request);

        assertTrue(response instanceof LoginResponse);
        LoginResponse loginResponse = (LoginResponse) response;
        assertEquals("validUser", loginResponse.username);
    }

    @Test
    void testUnsuccessfulLogin() {
        LoginRequest request = new LoginRequest("invalidUser", "invalidPassword");

        Response response = UserService.login(request);

        assertTrue(response instanceof FailureResponse);
        FailureResponse failureResponse = (FailureResponse) response;
    }

    @Test
    void testRepeatLoginUniqueAuthToken() {
        RegistrationService.register(new RegisterRequest("repeatUser", "repeatPassword", ""));
        LoginRequest request = new LoginRequest("repeatUser", "repeatPassword");

        Response response1 = UserService.login(request);
        Response response2 = UserService.login(request);

        assertTrue(response1 instanceof LoginResponse);
        assertTrue(response2 instanceof LoginResponse);
        LoginResponse loginResponse1 = (LoginResponse) response1;
        LoginResponse loginResponse2 = (LoginResponse) response2;
        assertEquals("repeatUser", loginResponse1.username);
        assertEquals("repeatUser", loginResponse2.username);
        assertNotEquals(loginResponse1.authToken, loginResponse2.authToken);
    }


    @Test
    void testUnsuccessfulLogout() {
        var registerResult = RegistrationService.register(new RegisterRequest("validUser", "validPass", "validEmail"));
        var authToken = "invalidAuthToken";
        var logoutRequest = new LogoutRequest(authToken);

        Response response = UserService.logout(logoutRequest);

        assertTrue(response instanceof FailureResponse, "Response should be an instance of FailureResponse");

    }


}

