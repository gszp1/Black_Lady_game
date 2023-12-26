package server;

import exceptions.loginFailureException;
import exceptions.registrationFailureException;
import utils.Utils;

import java.util.Optional;
import java.util.regex.Matcher;

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

    public Optional<String> validateLoginCredentials(String email, String password) {
        if(email.isEmpty() || password.isEmpty()) {
            return Optional.of(loginFailureException.EMPTY_FIELDS);
        }
        Matcher matcher = Utils.pattern.matcher(email);
        if(!matcher.matches()) {
            return Optional.of(loginFailureException.INCORRECT_EMAIL);
        }
        return Optional.empty();
    }

    public Optional<String> validateRegistrationCredentials(String email, String username,
                                                             String password, String passwordConfirmation) {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
            return Optional.of(registrationFailureException.EMPTY_FIELDS);
        }
        if(!passwordConfirmation.equals(password)) {
            return Optional.of(registrationFailureException.PASSWORDS_NOT_EQUAL);
        }
        Matcher matcher = Utils.pattern.matcher(email);
        if(!matcher.matches()) {
            return Optional.of(registrationFailureException.INCORRECT_EMAIL);
        }

        return Optional.empty();
    }

}

