package reviewme.be.util.exception;

public class NotLoggedInUserException extends RuntimeException {

    public NotLoggedInUserException(String message) {

        super(message);
    }
}
