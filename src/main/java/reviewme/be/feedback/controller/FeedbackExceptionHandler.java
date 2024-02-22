package reviewme.be.feedback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.feedback.exception.NonExistFeedbackException;
import reviewme.be.feedback.exception.NotParentFeedbackException;

@RestControllerAdvice
public class FeedbackExceptionHandler {

    @ExceptionHandler(NonExistFeedbackException.class)
    public ResponseEntity<CustomErrorResponse> nonExistFeedback(NonExistFeedbackException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(NotParentFeedbackException.class)
    public ResponseEntity<CustomErrorResponse> notParentFeedbac(NotParentFeedbackException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }
}
