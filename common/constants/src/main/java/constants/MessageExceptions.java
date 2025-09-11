package constants;

public final class MessageExceptions {


    private MessageExceptions() {
        throw new IllegalStateException(MSG_UTILITY_CLASS);
    }

    public static final String MSG_NOT_FOUND = "The entity with identifier %s was not found.";
    public static final String MSG_UTILITY_CLASS = "The entity with identifier %s already exists.";
    public static final String MSG_CREDENTIALS_INVALID = "The provided credentials are invalid.";
    public static final String MSG_UNAUTHORIZED = "Authentication required or token invalid";
    public static final String MSG_FORBIDDEN = "Oops! It looks like you’re trying to access a restricted area. Unfortunately, you don’t have the proper clearance. Better luck next time!";

}
