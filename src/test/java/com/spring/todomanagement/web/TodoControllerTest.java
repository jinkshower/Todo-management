package com.spring.todomanagement.web;

import com.spring.todomanagement.auth.JwtUtil;
import com.spring.todomanagement.domain.todo.Todo;
import com.spring.todomanagement.domain.todo.TodoRepository;
import com.spring.todomanagement.web.dto.LoginRequestDto;
import com.spring.todomanagement.web.dto.SignupRequestDto;
import com.spring.todomanagement.web.dto.TodoUpdateRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
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

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TodoRepository todoRepository;

    private Long userId;
    private String validToken1;
    private String validToken2;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        TokenResponse tokenResponse1 = getValidUserInfo("hiyen", "12345678");
        TokenResponse tokenResponse2 = getValidUserInfo("jackie", "87654321");
        userId = tokenResponse1.getUserId();
        validToken1 = tokenResponse1.getToken();
        validToken2 = tokenResponse2.getToken();
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
        ExtractableResponse<Response> response = requestTodoPost(bodyMap(), validToken1);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Todo> foundTodos = todoRepository.findAllByUserId(userId);
        assertThat(foundTodos.get(0).getUser().getId()).isEqualTo(userId);
    }

    @Disabled
    @DisplayName("모든 할일 목록을 조회할 수 있다")
    @Test
    void test3() {
        //given
        requestTodoPost(bodyMap(), validToken1);
        requestTodoPost(bodyMap(), validToken1);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/api/todos")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> list = response.jsonPath().getList("data");
        assertThat(list.size()).isEqualTo(2);
    }

    @DisplayName("특정 할일을 id로 조회할 수 있다")
    @Test
    void test4() {
        //given
        requestTodoPost(bodyMap(), validToken1);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/api/todos/1")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("hiyen", "1", "myTitle", "myContent");
    }

    @DisplayName("토큰을 가지고 할일의 userId와 동일한 id를 가진 유저는 할일을 수정할 수 있다")
    @Test
    void test5() {
        //given
        requestTodoPost(bodyMap(), validToken1);
        TodoUpdateRequestDto requestDto = TodoUpdateRequestDto.builder()
                .title("updateTitle")
                .content("updateContent")
                .build();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", validToken1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().patch("/api/todos/1")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("hiyen", "1", "updateTitle", "updateContent");
    }

    @DisplayName("토큰을 가졌지만 할일의 userId와 동일하지 않은 id를 가진 유저는 할 일을 수정 할 수 없다")
    @Test
    void test6() {
        //given
        requestTodoPost(bodyMap(), validToken1);
        TodoUpdateRequestDto requestDto = TodoUpdateRequestDto.builder()
                .title("updateTitle")
                .content("updateContent")
                .build();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", validToken2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().patch("/api/todos/1")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("올바른 유저가 아닙니다");
    }

    @DisplayName("토큰을 가지고 할일의 userId와 동일한 id를 가진 유저는 할일 상태를 변경할 수 있다")
    @Test
    void test7() {
        //given
        requestTodoPost(bodyMap(), validToken1);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", validToken1)
                .when().patch("/api/todos/1/status")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("1");
    }

    @DisplayName("토큰을 가졌지만 할일의 userId와 동일하지 않은 id를 가진 유저는 할 일을 수정 할 수 없다")
    @Test
    void test8() {
        //given
        requestTodoPost(bodyMap(), validToken1);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", validToken2)
                .when().patch("/api/todos/1/status")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("올바른 유저가 아닙니다");
    }

    @AfterEach
    void clear() {
        todoRepository.deleteAll();
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
        String token = loginResponse.header(JwtUtil.AUTHORIZATION_HEADER);
        Long userId = extractIdFromToken(token);
        return new TokenResponse(userId, token);
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

    private Long extractIdFromToken(String token) {
        String substring = token.substring(7);
        return jwtUtil.getUserIdFromToken(substring);
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