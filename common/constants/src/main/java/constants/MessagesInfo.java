package constants;

import static constants.MessageExceptions.*;

public class MessagesInfo {

    private MessagesInfo() {
        throw new IllegalStateException(MSG_UTILITY_CLASS);
    }

    public static final String MSG_OPERATION_SUCCESS = "The operation was completed successfully.";
    public static final String MSG_OPERATION_FAILED = "The operation has failed.";
    public static final String MSG_INVALID_INPUT = "The input provided is invalid.";
    public static final String MSG_ACCESS_DENIED = "Access is denied.";
    public static final String MSG_LOGIN_SUCCESS = "User logged in successfully :).";

}