package reviewme.be.login.exception;

public class InvalidCodeException extends RuntimeException {

    public InvalidCodeException(String message) {

        super(message);
    }
}
