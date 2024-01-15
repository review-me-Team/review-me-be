package reviewme.be.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.user.exception.InvalidCodeException;
import reviewme.be.user.exception.NonExistUserException;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<CustomErrorResponse> invalidCode(InvalidCodeException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(NonExistUserException.class)
    public ResponseEntity<CustomErrorResponse> nonExistUser(NonExistUserException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }
}
