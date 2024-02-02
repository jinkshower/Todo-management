package com.spring.todomanagement.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("회원가입 실패");
        return ResponseEntity.badRequest().body(CommonResponse.<String>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .data(message).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.info("서비스 검증 실패");
        return ResponseEntity.badRequest().body(CommonResponse.<String>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .data(e.getMessage()).build());
    }
}
