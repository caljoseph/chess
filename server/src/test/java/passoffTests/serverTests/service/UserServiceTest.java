package passoffTests.serverTests.service;

import model.*;
import org.junit.jupiter.api.Test;
import service.RegistrationService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void testSuccessfulLogin() {
        RegistrationService.register(new RegisterRequest("validUser", "validPassword", ""));
        LoginRequest request = new LoginRequest("validUser", "validPassword");

        Response response = UserService.login(request);

        assertTrue(response instanceof LoginResponse);
        LoginResponse loginResponse = (LoginResponse) response;
        assertEquals("validUser", loginResponse.username);
        assertNotNull(loginResponse.authToken);
    }

    @Test
    void testUnsuccessfulLogin() {
        LoginRequest request = new LoginRequest("invalidUser", "invalidPassword");

        Response response = UserService.login(request);

        assertTrue(response instanceof FailureResponse);
        FailureResponse failureResponse = (FailureResponse) response;
        assertEquals("Error: unauthorized", failureResponse.getMessage());
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
}

