package com.cnotar7.projects.aigolfcompanion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.MissingResourceException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponseException(HttpStatus.CONFLICT);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingResourceException.class)
    public ResponseEntity<ErrorResponse> handleMissingResource(MissingResourceException ex) {
        ErrorResponse error = new ErrorResponseException(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<ErrorResponse> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        ErrorResponse error = new ErrorResponseException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
