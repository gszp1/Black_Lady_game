package server;

import exceptions.loginFailureException;
import exceptions.registrationFailureException;
import utils.Utils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameServer {

    private void registerUser(String email, String username, String password, String passwordConfirmation)
            throws registrationFailureException {
        Optional<String> validationResult = validateRegistrationCredentials(email, username,
                password, passwordConfirmation);
        if(validationResult.isPresent()) {
            throw(new registrationFailureException(validationResult.get()));
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new registrationFailureException("Failed to connect with service.");
        }

    }

    private void loginUser(String email, String password) throws loginFailureException {
        Optional<String> validationResult = validateLoginCredentials(email, password);
        if(validationResult.isPresent()) {
            throw(new loginFailureException(validationResult.get()));
        }
    }

    private Optional<String> validateLoginCredentials(String email, String password) {
        if(email.isEmpty() || password.isEmpty()) {
            return Optional.of("Not all login data provided.");
        }
        Matcher matcher = Utils.pattern.matcher(email);
        if(!matcher.matches()) {
            return Optional.of("Provided Email is incorrect.");
        }
        return Optional.empty();
    }

    private Optional<String> validateRegistrationCredentials(String email, String username,
                                                             String password, String passwordConfirmation) {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
            return Optional.of("Not all registration data provided.");
        }
        if(!passwordConfirmation.equals(password)) {
            return Optional.of("Provided passwords are not equal.");
        }
        Matcher matcher = Utils.pattern.matcher(email);
        if(!matcher.matches()) {
            return Optional.of("Provided Email is incorrect.");
        }

        return Optional.empty();
    }

}

