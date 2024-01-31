package com.spring.todomanagement.web;

import com.spring.todomanagement.service.user.UserService;
import com.spring.todomanagement.web.dto.AuthRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody AuthRequestDto requestDto) {
        userService.signup(requestDto);
        log.info("회원등록");
        return ResponseEntity.ok().body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("회원등록 되었습니다.").build());
    }

    @PostMapping("/user/login")
    public ResponseEntity<CommonResponse> login(@RequestBody AuthRequestDto requestDto, HttpServletResponse response) {
        userService.login(requestDto, response);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("로그인 되었습니다.").build());
    }
}
