package reviewme.be.friend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.friend.exception.AlreadyFriendRelationException;
import reviewme.be.friend.exception.AlreadyFriendRequestedException;
import reviewme.be.friend.exception.NotOnTheFriendRelationException;
import reviewme.be.friend.exception.NotOnTheFriendRequestException;

@RestControllerAdvice
public class FriendExceptionHandler {

    @ExceptionHandler(AlreadyFriendRequestedException.class)
    public ResponseEntity<CustomErrorResponse> alreadyFriendRequested(AlreadyFriendRequestedException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(AlreadyFriendRelationException.class)
    public ResponseEntity<CustomErrorResponse> alreadyFriendRelation(AlreadyFriendRelationException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(NotOnTheFriendRequestException.class)
    public ResponseEntity<CustomErrorResponse> notOnTheFriendRequest(NotOnTheFriendRequestException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(NotOnTheFriendRelationException.class)
    public ResponseEntity<CustomErrorResponse> notOnTheFriendRelation(NotOnTheFriendRelationException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }
}
