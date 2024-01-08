package reviewme.be.util.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.util.exception.NonExistScopeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "fail",
                        400,
                        e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(NonExistScopeException.class)
    public ResponseEntity<CustomErrorResponse> nonExistScope(NonExistScopeException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }
}
