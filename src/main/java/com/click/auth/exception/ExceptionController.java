package com.click.auth.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String feignException(FeignException e) {
        return e.getMessage();
    }

    @ExceptionHandler(PasswordMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String passwordException() {
        return "WRONG PASSWORD";
    }

    @ExceptionHandler(LoginExpirationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String LoginExpirationException() {
        return "LOGIN INFO HAS EXPIRED";
    }
}
