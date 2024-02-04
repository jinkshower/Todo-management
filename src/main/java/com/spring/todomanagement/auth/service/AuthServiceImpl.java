package com.spring.todomanagement.auth.service;

import com.spring.todomanagement.auth.exception.AuthenticationException;
import com.spring.todomanagement.auth.exception.LoginFailedException;
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
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void signup(SignupRequestDto requestDto) {
        String name = requestDto.getName();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> foundUserByName = userRepository.findByName(name);
        if (foundUserByName.isPresent()) {
            String errorMessage = "회원이름 중복입니다. 요청 이름: " + name;
            log.error(errorMessage);
            throw new AuthenticationException(errorMessage);
        }
        userRepository.save(User.builder()
                .name(name)
                .password(password)
                .build());
    }

    @Override
    public String login(LoginRequestDto requestDto) {
        String name = requestDto.getName();
        String password = requestDto.getPassword();

        User user = userRepository.findByName(name).orElseThrow(
                () -> {
                    String errorMessage = "이름을 찾을 수 없습니다. 요청 이름: " + name;
                    log.error(errorMessage);
                    return new LoginFailedException(errorMessage);
                }
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            String errorMessage = "비밀번호가 다릅니다. 요청 이름: " + name;
            log.error(errorMessage);
            throw new LoginFailedException(errorMessage);
        }

        return jwtUtil.createToken(user.getId());
    }
}
