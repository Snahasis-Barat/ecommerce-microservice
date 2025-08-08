package com.example.api_gateway.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> UserUnauthenticatedException(UserNotAuthenticatedException ex)
    {
        return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
    }
}
