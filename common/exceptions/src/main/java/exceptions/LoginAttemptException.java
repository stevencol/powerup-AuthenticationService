package exceptions;

public class LoginAttemptException extends RuntimeException {
    public LoginAttemptException(String message) {
        super(message);
    }
}
