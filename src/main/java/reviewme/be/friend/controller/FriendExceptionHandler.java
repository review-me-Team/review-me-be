package reviewme.be.friend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.friend.exception.AlreadyFriendException;
import reviewme.be.friend.exception.AlreadyFriendRequestedException;

@RestControllerAdvice
public class FriendExceptionHandler {

    @ExceptionHandler(AlreadyFriendRequestedException.class)
    public ResponseEntity<CustomErrorResponse> invalidCode(AlreadyFriendRequestedException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(AlreadyFriendException.class)
    public ResponseEntity<CustomErrorResponse> invalidCode(AlreadyFriendException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }
}
