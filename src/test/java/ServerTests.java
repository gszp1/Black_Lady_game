import org.junit.Test;
import server.GameServer;
import exceptions.*;

import java.util.Optional;

import static org.junit.Assert.*;

public class ServerTests {
    @Test
    public void testRegisterValidation() {
        GameServer gameServer = new GameServer();

    }

    @Test
    public void testLoginValidation() {
        GameServer gameServer = new GameServer();
        assertFalse(gameServer.validateLoginCredentials("test@gmail.com", "password").isPresent());

        Optional<String> result = gameServer.validateLoginCredentials("", "");
        assertTrue(result.isPresent());
        assertEquals(result.get(), loginFailureException.EMPTY_FIELDS);

        result = gameServer.validateLoginCredentials("test@gmail.com", "");
        assertTrue(result.isPresent());
        assertEquals(result.get(), loginFailureException.EMPTY_FIELDS);

        result = gameServer.validateLoginCredentials("", "password");
        assertTrue(result.isPresent());
        assertEquals(result.get(), loginFailureException.EMPTY_FIELDS);

        result = gameServer.validateLoginCredentials("testgmail.com", "password");
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
