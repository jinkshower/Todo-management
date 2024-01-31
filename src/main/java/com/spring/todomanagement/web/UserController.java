package com.spring.todomanagement.web;

import com.spring.todomanagement.service.user.UserService;
import com.spring.todomanagement.web.dto.AuthRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody AuthRequestDto requestDto, RedirectAttributes redirectAttributes) {
        userService.signup(requestDto);
        return ResponseEntity.ok().body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("회원등록 되었습니다.").build());
    }
}
