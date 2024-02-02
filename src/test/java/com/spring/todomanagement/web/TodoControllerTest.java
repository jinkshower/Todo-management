package com.spring.todomanagement.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.spring.todomanagement.auth.JwtUtil;
import com.spring.todomanagement.domain.todo.Todo;
import com.spring.todomanagement.domain.todo.TodoRepository;
import com.spring.todomanagement.web.dto.LoginRequestDto;
import com.spring.todomanagement.web.dto.SignupRequestDto;
import com.spring.todomanagement.web.dto.TodoResponseDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TodoRepository todoRepository;

    private Long userId;
    private String validToken;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        TokenResponse tokenResponse = getValidUserInfo("hiyen", "12345678");
        userId = tokenResponse.getUserId();
        validToken = tokenResponse.getToken();
    }

    @DisplayName("토큰 검증을 통과하지 못한 유저는 일정을 등록할 수 없다")
    @Test
    void test1() {
        //given
        String token = "1234";

        //when
        ExtractableResponse<Response> response = requestTodoPost(bodyMap(), token);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("토큰 검증을 통과한 유저는 일정을 등록할 수 있다")
    @Test
    void test2() {
        //given //when
        ExtractableResponse<Response> response = requestTodoPost(bodyMap(), validToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Todo> foundTodos = todoRepository.findAllByUserId(userId);
        assertThat(foundTodos.get(0).getUser().getId()).isEqualTo(userId);
    }

    @DisplayName("모든 할일 목록을 조회할 수 있다")
    @Test
    void test3() {
        //given
        requestTodoPost(bodyMap(), validToken);
        requestTodoPost(bodyMap(), validToken);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/api/todos")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> list = response.jsonPath().getList("data");
        assertThat(list.size()).isEqualTo(3);
    }

    private ExtractableResponse<Response> requestTodoPost(Map<String, Object> bodyMap, String accessToken) {
        return RestAssured.given().log().all()
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bodyMap)
                .when().post("/api/todos")
                .then().log().all()
                .extract();
    }

    private TokenResponse getValidUserInfo(String name, String password) {
        signup(name, password);
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .name(name)
                .password(password)
                .build();
        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                .body(loginRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/auth/login")
                .then().log().all()
                .extract();
        Integer id = loginResponse.body().jsonPath().get("data");
        String token = loginResponse.header(JwtUtil.AUTHORIZATION_HEADER);
        return new TokenResponse(id.longValue(), token);
    }

    private void signup(String name, String password) {
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .name(name)
                .password(password)
                .build();
        ExtractableResponse<Response> signupResponse = RestAssured.given().log().all()
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/auth/signup")
                .then().log().all()
                .extract();
    }

    private Map<String, Object> bodyMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "hiyen");
        map.put("title", "myTitle");
        map.put("content", "myContent");
        return map;
    }

    class TokenResponse {
        private Long userId;

        private String token;

        public TokenResponse(Long userId, String token) {
            this.userId = userId;
            this.token = token;
        }

        public Long getUserId() {
            return userId;
        }

        public String getToken() {
            return token;
        }
    }
}