package reviewme.be.user.exception;

public class NoAuthorizationException extends RuntimeException {

        public NoAuthorizationException(String message) {
            super(message);
        }
}
