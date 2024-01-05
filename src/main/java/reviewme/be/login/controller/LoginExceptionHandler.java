package reviewme.be.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.login.exception.InvalidCodeException;

@RestControllerAdvice
public class LoginExceptionHandler {

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<CustomErrorResponse> invalidCode(InvalidCodeException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }
}
