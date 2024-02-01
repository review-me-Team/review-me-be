package reviewme.be.user.exception;

public class NoValidBearerFormatException extends RuntimeException {

        public NoValidBearerFormatException(String message) {
            super(message);
        }
}
