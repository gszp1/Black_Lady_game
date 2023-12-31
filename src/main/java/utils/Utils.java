package utils;

import java.util.regex.Pattern;

/**
 * Class for universal code, not belonging specifically to any other class.
 * Contains pattern for email validation and compiled pattern.
 */
public class Utils {
    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

}
