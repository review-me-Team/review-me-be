package reviewme.be.user.exception;

public class InvalidJWTException extends RuntimeException {

    public InvalidJWTException(String message) {

        super(message);
    }
}
