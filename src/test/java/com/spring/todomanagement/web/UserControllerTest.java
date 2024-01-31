package com.spring.todomanagement.web;

import com.spring.todomanagement.auth.JwtUtil;
import com.spring.todomanagement.domain.user.Role;
import com.spring.todomanagement.domain.user.User;
import com.spring.todomanagement.domain.user.UserRepository;
import com.spring.todomanagement.web.dto.AuthRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
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
    void test() {
        //given
        String name = "Jackie";
        String password = "1234";

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .name(name)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/user/signup";

        //when
        ResponseEntity<AuthRequestDto> responseEntity =
                restTemplate.postForEntity(url, requestDto, AuthRequestDto.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<User> all = userRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(name);
    }

    @DisplayName("로그인을 할 수 있다")
    @Test
    void test1() {
        //given
        String name = "Jack";
        String password = "1234";

        userRepository.save(User.builder()
                .name(name)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build());

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .name(name)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/user/login";

        //when
        ResponseEntity<AuthRequestDto> responseEntity =
                restTemplate.postForEntity(url, requestDto, AuthRequestDto.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().keySet()).contains(JwtUtil.AUTHORIZATION_HEADER);
    }
}