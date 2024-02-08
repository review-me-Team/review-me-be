package reviewme.be.question.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reviewme.be.custom.CustomErrorResponse;
import reviewme.be.resume.exception.BadFileExtensionException;
import reviewme.be.resume.exception.NonExistResumeException;
import reviewme.be.resume.exception.NotYourResumeException;

@RestControllerAdvice
public class QuestionExceptionHandler {


}
