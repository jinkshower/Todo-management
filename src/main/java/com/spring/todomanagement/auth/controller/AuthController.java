package com.spring.todomanagement.auth.controller;

import com.spring.todomanagement.auth.dto.LoginRequestDto;
import com.spring.todomanagement.auth.dto.SignupRequestDto;
import com.spring.todomanagement.auth.service.AuthService;
import com.spring.todomanagement.auth.support.JwtUtil;
import com.spring.todomanagement.common.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<CommonResponse<String>> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        authService.signup(requestDto);
        return ResponseEntity.ok().body(CommonResponse.<String>builder()
                .statusCode(HttpStatus.CREATED.value())
                .data("회원등록 되었습니다").build());
    }

    @PostMapping("/auth/login")
    public ResponseEntity<CommonResponse<String>> login(@RequestBody @Valid LoginRequestDto requestDto,
                                                        HttpServletResponse response) {
        String createdToken = authService.login(requestDto);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createdToken);
        return ResponseEntity.ok().body(CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .data("로그인 되었습니다").build());
    }
}
