package reviewme.be.user.exception;

public class NoRefreshTokenException extends RuntimeException {

    public NoRefreshTokenException(String message) {

        super(message);
    }
}
