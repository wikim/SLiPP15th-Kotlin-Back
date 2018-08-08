package net.slipp.fifth.kotlin.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private static Pattern pattern;
    private static Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    static {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Validate email by EMAIL_PATTERN
     * 
     * @param email
     * @return (true: valid, false: invalid)
     */
    public static boolean validate(final String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
