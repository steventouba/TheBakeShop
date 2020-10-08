package com.steven.willysbakeshop.controller;


import com.steven.willysbakeshop.utilities.exceptions.ErrorDetails;
import com.steven.willysbakeshop.utilities.exceptions.UserNotFoundException;
import com.steven.willysbakeshop.utilities.exceptions.UserValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

   @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
    ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserValidationException.class)
    public final ResponseEntity<ErrorDetails> handleUserValidationException(UserValidationException ex, WebRequest request) {
       ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));

       return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}