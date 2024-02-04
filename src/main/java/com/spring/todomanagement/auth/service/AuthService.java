package com.spring.todomanagement.auth.service;

import com.spring.todomanagement.auth.support.JwtUtil;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import com.spring.todomanagement.auth.dto.LoginRequestDto;
import com.spring.todomanagement.auth.dto.SignupRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto requestDto) {
        String name = requestDto.getName();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> foundUserByName = userRepository.findByName(name);
        if (foundUserByName.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        userRepository.save(User.builder()
                .name(name)
                .password(password)
                .build());
    }

    public void login(LoginRequestDto requestDto, HttpServletResponse response) {
        String name = requestDto.getName();
        String password = requestDto.getPassword();

        User user = userRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getId());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    }
}
