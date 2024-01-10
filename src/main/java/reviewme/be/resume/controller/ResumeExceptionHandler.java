package reviewme.be.resume.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.resume.exception.BadFileExtensionException;
import reviewme.be.resume.exception.NonExistResumeException;

@RestControllerAdvice
public class ResumeExceptionHandler {

    @ExceptionHandler(BadFileExtensionException.class)
    public ResponseEntity<CustomErrorResponse> badFileExtension(BadFileExtensionException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(NonExistResumeException.class)
    public ResponseEntity<CustomErrorResponse> nonExistResume(NonExistResumeException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Not Found",
                        404,
                        ex.getMessage()));
    }
}
