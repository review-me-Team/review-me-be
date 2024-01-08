package reviewme.be.user.exception;

public class NonExistUserException extends RuntimeException {

    public NonExistUserException(String message) {

        super(message);
    }
}
