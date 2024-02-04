package com.spring.todomanagement.auth.exception;

public class InvalidTokenException extends AuthenticationException{

    public InvalidTokenException(String message) {
        super(message);
    }
}
