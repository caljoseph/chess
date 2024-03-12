package service;

import model.response.FailureResponse;
import model.request.RegisterRequest;
import model.response.RegisterResponse;
import model.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationServiceTest {
    @Test
    public void testValidRegistration() {
        RegisterRequest validRequest = new RegisterRequest("newUser", "password123", "user@example.com");
        Response response = RegistrationService.register(validRequest);
        assertTrue(response instanceof RegisterResponse, "Valid registration should return RegisterResponse");
    }

    @Test
    public void testInvalidRegistrationMissingField() {
        RegisterRequest invalidRequest = new RegisterRequest(null, "password123", "user@example.com");
        Response response = RegistrationService.register(invalidRequest);
        assertTrue(response instanceof FailureResponse, "Invalid registration should return FailureResponse");
        assertEquals("Error: bad request", ((FailureResponse) response).message, "Error message should be 'Error: bad request'");
    }

    @Test
    public void testUsernameAlreadyTaken() {
        RegisterRequest firstRequest = new RegisterRequest("existingUser", "difpassword123", "difuser@example.com");
        RegisterRequest secondRequest = new RegisterRequest("existingUser", "password123", "user@example.com");
        Response firstResponse =  RegistrationService.register(firstRequest);
        Response secondResponse = RegistrationService.register(secondRequest);
        assertTrue(secondResponse instanceof FailureResponse, "Registration with already taken username should return FailureResponse");
        assertEquals("Error: already taken", ((FailureResponse) secondResponse).message, "Error message should be 'Error: already taken'");
    }


}

