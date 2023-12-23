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
        if(!passwordConfirmation.equals(password)) {
            return Optional.of("Provided passwords are not equal.");
        }
        if(!username.isEmpty()) {
            return Optional.of("Username not provided.");
        }
        Matcher matcher = GameServer.pattern.matcher(email);
        if(!matcher.matches()) {
            return Optional.of("Provided Email is incorrect.");
        }
        return Optional.empty();
    }

}


