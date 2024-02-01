package com.spring.todomanagement.web;

import com.spring.todomanagement.auth.JwtUtil;
import com.spring.todomanagement.domain.user.Role;
import com.spring.todomanagement.domain.user.User;
import com.spring.todomanagement.domain.user.UserRepository;
import com.spring.todomanagement.web.dto.LoginRequestDto;
import com.spring.todomanagement.web.dto.SignupRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @DisplayName("회원가입을 할 수 있다")
    @Test
    void test1() {
        //given
        String name = "jackie";
        String password = "12345678";

        SignupRequestDto requestDto = SignupRequestDto.builder()
                .name(name)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/user/signup";

        //when
        ResponseEntity<CommonResponse> responseEntity =
                restTemplate.postForEntity(url, requestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<User> all = userRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(name);
    }

    @DisplayName("로그인을 할 수 있다")
    @Test
    void test2() {
        //given
        String name = "Jack";
        String password = "12345678";

        userRepository.save(User.builder()
                .name(name)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build());

        LoginRequestDto requestDto = LoginRequestDto.builder()
                .name(name)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/user/login";

        //when
        ResponseEntity<CommonResponse> responseEntity =
                restTemplate.postForEntity(url, requestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().keySet()).contains(JwtUtil.AUTHORIZATION_HEADER);
    }

    @DisplayName("올바른 이름이 아니면 회원가입 할 수 없다")
    @ParameterizedTest
    @ValueSource(strings = {"Jack", "!jack", "aaa", "aaaaaaaaaaa"})
    void test3(String input) {
        String password = "12345678";

        SignupRequestDto requestDto = SignupRequestDto.builder()
                .name(input)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/user/signup";

        //when
        ResponseEntity<CommonResponse> responseEntity =
                restTemplate.postForEntity(url, requestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("올바른 비밀번호가 아니면 회원가입 할 수 없다")
    @ParameterizedTest
    @ValueSource(strings = {"1234567", "!password", "aaaaaaaaaaaaaaaa"})
    void test4(String input) {
        String name = "jack";

        SignupRequestDto requestDto = SignupRequestDto.builder()
                .name(name)
                .password(input)
                .build();

        String url = "http://localhost:" + port + "/user/signup";

        //when
        ResponseEntity<CommonResponse> responseEntity =
                restTemplate.postForEntity(url, requestDto, CommonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}