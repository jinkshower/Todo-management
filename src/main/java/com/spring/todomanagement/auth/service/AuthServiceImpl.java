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
            log.error("회원이름 중복");
            throw new AuthenticationException("이미 존재하는 회원이름입니다.");
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
                    log.error("이름을 찾을 수 없습니다.");
                    return new LoginFailedException();
                }
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("비밀번호가 다릅니다.");
            throw new LoginFailedException();
        }

        return jwtUtil.createToken(user.getId());
    }
}
