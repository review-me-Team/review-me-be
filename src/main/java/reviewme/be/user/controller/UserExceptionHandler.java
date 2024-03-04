package reviewme.be.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.user.exception.*;

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
                        "Not Found",
                        404,
                        ex.getMessage()));
    }

    @ExceptionHandler(NoSearchConditionException.class)
    public ResponseEntity<CustomErrorResponse> noSearchCondition(NoSearchConditionException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(NoRefreshTokenException.class)
    public ResponseEntity<CustomErrorResponse> noRefreshToken(NoRefreshTokenException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(NoValidBearerFormatException.class)
    public ResponseEntity<CustomErrorResponse> noValidBearerFormat(NoValidBearerFormatException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<CustomErrorResponse> invalidToken(InvalidTokenException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(ManipulatedTokenException.class)
    public ResponseEntity<CustomErrorResponse> manipulatedToken(ManipulatedTokenException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<CustomErrorResponse> httpClientErrorException(HttpClientErrorException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Bad Request",
                        400,
                        "유효하지 않은 refresh token입니다. 다시 로그인해주세요."));
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<CustomErrorResponse> expiredTokenException(ExpiredTokenException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Unauthorized",
                        401,
                        ex.getMessage()));
    }

    @ExceptionHandler(NoAuthorizationException.class)
    public ResponseEntity<CustomErrorResponse> noAuthorizationException(NoAuthorizationException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "Forbidden",
                        403,
                        ex.getMessage()));
    }
}
