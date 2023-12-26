package server;

import exceptions.loginFailureException;
import exceptions.registrationFailureException;
import org.apache.commons.codec.digest.DigestUtils;
import utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Matcher;

public class GameServer {

    public static void main(String [] args) {
        System.out.println("Hello world");
    }

    private void registerUser(String email, String username, String password, String passwordConfirmation)
            throws registrationFailureException {
        Optional<String> validationResult = validateRegistrationCredentials(email, username,
                password, passwordConfirmation);
        if(validationResult.isPresent()) {
            throw(new registrationFailureException(validationResult.get()));
        }
        String hashPassword = DigestUtils.md5Hex(password).toUpperCase();
    }

    private void loginUser(String email, String password) throws loginFailureException {
        Optional<String> validationResult = validateLoginCredentials(email, password);
        if(validationResult.isPresent()) {
            throw(new loginFailureException(validationResult.get()));
        }
        String hashPassword = DigestUtils.md5Hex(password).toUpperCase();
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

