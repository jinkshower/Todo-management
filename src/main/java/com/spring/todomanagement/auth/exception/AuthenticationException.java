package com.spring.todomanagement.auth.exception;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException() {
        super("사용자 인증에 실패했습니다.");
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
