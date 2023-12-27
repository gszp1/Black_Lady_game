import org.junit.Test;
import server.GameServer;
import exceptions.*;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Set of tests for GameServer methods
 */
public class ServerTests {
    /**
     *  Tests for registration credentials validation
     */
    @Test
    public void testRegisterValidation(){
        GameServer gameServer = new GameServer();
        assertFalse(gameServer.validateRegistrationCredentials("test@gmail.com", "test",
                "password", "password").isPresent());

        testRegisterValidationEmptyFields(gameServer);
        testRegisterValidationPasswordsNotEqual(gameServer);
        testRegisterValidationIncorrectEmail(gameServer);
    }


    /**
     * Subset of tests for registration credentials validation, which test Incorrect emails
     * @param gameServer - GameServer object
     */
    public void testRegisterValidationIncorrectEmail(GameServer gameServer) {
        Optional<String> result = gameServer.validateRegistrationCredentials("testgmail.com", "test",
                "password",  "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.INCORRECT_EMAIL);

        result = gameServer.validateRegistrationCredentials("test@gmail", "test",
                "password", "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.INCORRECT_EMAIL);

        result = gameServer.validateRegistrationCredentials("@gmail.com", "test",
                "password", "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.INCORRECT_EMAIL);
    }

    /**
     * Subset of tests for registration credentials validation, which test not equal passwords
     * @param gameServer - GameServer object
     */
    public void testRegisterValidationPasswordsNotEqual(GameServer gameServer) {
        Optional<String> result = gameServer.validateRegistrationCredentials("test@gmail.com", "test",
                "password", "pass");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.PASSWORDS_NOT_EQUAL);

        result = gameServer.validateRegistrationCredentials("test@gmail.com", "test",
                "pass", "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.PASSWORDS_NOT_EQUAL);
    }

    /**
     * Subset of tests for registration credentials validation, which test not equal passwords
     * @param gameServer - GameServer object
     */
    public void testRegisterValidationEmptyFields(GameServer gameServer) {
        Optional<String> result = gameServer.validateRegistrationCredentials("", "", "",
                "");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.EMPTY_FIELDS);

        result = gameServer.validateRegistrationCredentials("test@gmail.com", "test", "password",
                "");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.EMPTY_FIELDS);

        result = gameServer.validateRegistrationCredentials("test@gmail.com", "test", "",
                "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.EMPTY_FIELDS);

        result = gameServer.validateRegistrationCredentials("test@gmail.com", "", "password",
                "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.EMPTY_FIELDS);

        result = gameServer.validateRegistrationCredentials("", "test", "password",
                "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), registrationFailureException.EMPTY_FIELDS);
    }

    /**
     * Tests for login credentials validation
     */
    @Test
    public void testLoginValidation()  {
        GameServer gameServer = new GameServer();
        assertFalse(gameServer.validateLoginCredentials("test@gmail.com", "password").isPresent());
        testLoginValidationEmptyFields(gameServer);
        testLoginValidationIncorrectEmail(gameServer);
    }

    /**
     * Subset of tests for login credentials validation, which test empty fields
     * @param gameServer - GameServer object
     */
    public void testLoginValidationEmptyFields(GameServer gameServer) {
        Optional<String> result = gameServer.validateLoginCredentials("", "");
        assertTrue(result.isPresent());
        assertEquals(result.get(), loginFailureException.EMPTY_FIELDS);

        result = gameServer.validateLoginCredentials("test@gmail.com", "");
        assertTrue(result.isPresent());
        assertEquals(result.get(), loginFailureException.EMPTY_FIELDS);

        result = gameServer.validateLoginCredentials("", "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), loginFailureException.EMPTY_FIELDS);
    }

    /**
     * Subset of tests for login credentials validation, which test incorrect email
     * @param gameServer - GameServer object
     */
    public void testLoginValidationIncorrectEmail(GameServer gameServer) {
        Optional<String> result = gameServer.validateLoginCredentials("testgmail.com", "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), loginFailureException.INCORRECT_EMAIL);

        result = gameServer.validateLoginCredentials("test@gmail", "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), loginFailureException.INCORRECT_EMAIL);

        result = gameServer.validateLoginCredentials("@gmail.com", "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), loginFailureException.INCORRECT_EMAIL);
    }
}
