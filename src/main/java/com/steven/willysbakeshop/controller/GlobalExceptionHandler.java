package com.steven.willysbakeshop.controller;


import com.steven.willysbakeshop.utilities.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public final ResponseEntity<ValidationErrorResponse> handleValidationErrors(ConstraintViolationException exception) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();

        for (ConstraintViolation error : exception.getConstraintViolations()) {
            errorResponse.add(new Violation(error.getPropertyPath().toString(), error.getMessage()));
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public final ResponseEntity<ValidationErrorResponse> handleInvalidMethodArguments(
//            MethodArgumentNotValidException exception) {
//       ValidationErrorResponse errorResponse = new ValidationErrorResponse();
//
//       for (FieldError error : exception.getBindingResult().getFieldErrors()) {
//           errorResponse.add( new Violation(error.getField(), error.getDefaultMessage()));
//       }
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
}