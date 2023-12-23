package server;

import java.util.regex.Pattern;

public class GameServer {

    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
}
