package reviewme.be.user.exception;

public class ManipulatedTokenException extends RuntimeException {

    public ManipulatedTokenException(String message) {
        super(message);
    }
}
