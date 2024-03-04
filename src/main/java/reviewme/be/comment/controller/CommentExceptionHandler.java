package reviewme.be.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.comment.exception.NonExistCommentException;
import reviewme.be.custom.CustomErrorResponse;

@RestControllerAdvice
public class CommentExceptionHandler {

    @ExceptionHandler(NonExistCommentException.class)
    public ResponseEntity<CustomErrorResponse> nonExistComment(NonExistCommentException ex) {

        return ResponseEntity
            .badRequest()
            .body(new CustomErrorResponse(
                "Not Found",
                404,
                ex.getMessage()));
    }
}
