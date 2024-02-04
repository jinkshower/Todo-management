package com.spring.todomanagement.auth.service;

import com.spring.todomanagement.auth.dto.LoginRequestDto;
import com.spring.todomanagement.auth.dto.SignupRequestDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void signup(SignupRequestDto requestDto);
    String login(LoginRequestDto requestDto);
}
