package com.spring.todomanagement.auth.exception;

public class LoginFailedException extends AuthenticationException{

    public LoginFailedException(String message) {
        super(message);
    }
}
