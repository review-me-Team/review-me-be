package reviewme.be.user.exception;

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException(String message) {

        super(message);
    }
}
