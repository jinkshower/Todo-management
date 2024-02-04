package com.spring.todomanagement.auth.exception;

public class InvalidTokenException extends AuthenticationException{

    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }
}
