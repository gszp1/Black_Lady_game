package Client;

import Exceptions.registrationFailureException;
import Server.GameServer;

import java.util.Optional;
import java.util.regex.Matcher;

public class GameClient {

    public static void main(String [] args) {

    }

    private void registerUser(String email, String username, String password, String passwordConfirmation)
            throws registrationFailureException {
        Optional<String> validationResult = validateRegistrationCredentials(email, username,
                password, passwordConfirmation);
        if(validationResult.isPresent()) {
            throw(new registrationFailureException(validationResult.get()));
        }
    }

    private void loginUser(String email, String password) {

    }

    private Optional<String> validateRegistrationCredentials(String email, String username,
                                                             String password, String passwordConfirmation) {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
            return Optional.of("Not all registration data provided.");
        }
        if(!passwordConfirmation.equals(password)) {
            return Optional.of("Provided passwords are not equal.");
        }
        Matcher matcher = GameServer.pattern.matcher(email);
        if(!matcher.matches()) {
            return Optional.of("Provided Email is incorrect.");
        }

        return Optional.empty();
    }

}


