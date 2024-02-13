package reviewme.be.question.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.question.exception.NonExistQuestionException;

@RestControllerAdvice
public class QuestionExceptionHandler {

    @ExceptionHandler(NonExistQuestionException.class)
    public ResponseEntity<CustomErrorResponse> nonExistQuestionException(NonExistQuestionException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }
}
