package com.spring.todomanagement.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.spring.todomanagement.auth.dto.LoginRequestDto;
import com.spring.todomanagement.auth.dto.SignupRequestDto;
import com.spring.todomanagement.auth.support.JwtUtil;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.dto.TodoUpdateRequestDto;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TodoAcceptanceTest {

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
        ExtractableResponse<Response> response = postTodo(postInfo(), token);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("토큰 검증을 통과한 유저는 일정을 등록할 수 있다")
    @Test
    void test2() {
        //given //when
        ExtractableResponse<Response> response = postTodo(postInfo(), validToken1);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        List<Todo> foundTodos = todoRepository.findAllByUserId(userId);
        assertThat(foundTodos.get(0).getUser().getId()).isEqualTo(userId);
    }

    @Disabled
    @DisplayName("모든 할일 목록을 조회할 수 있다")
    @Test
    void test3() {
        //given
        postTodo(postInfo(), validToken1);
        postTodo(postInfo(), validToken1);

        //when
        ExtractableResponse<Response> response = getAllTodo();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> list = response.jsonPath().getList("data");
        assertThat(list.size()).isEqualTo(2);
    }

    @DisplayName("특정 할일을 id로 조회할 수 있다")
    @Test
    void test4() {
        //given
        ExtractableResponse<Response> postResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(postResponse);

        //when
        ExtractableResponse<Response> response = getTodo(todoId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("hiyen", "1", "myTitle", "myContent");
    }

    @DisplayName("토큰을 가지고 할일의 userId와 동일한 id를 가진 유저는 할일을 수정할 수 있다")
    @Test
    void test5() {
        //given
        postTodo(postInfo(), validToken1);
        String title = "updateTitle";
        String content = "updateContent";

        //when
        ExtractableResponse<Response> response = updateTodo(validToken1, title, content);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("hiyen", "1", "updateTitle",
            "updateContent");
    }

    @DisplayName("토큰을 가졌지만 할일의 userId와 동일하지 않은 id를 가진 유저는 할 일을 수정 할 수 없다")
    @Test
    void test6() {
        //given
        postTodo(postInfo(), validToken1);
        String title = "updateTitle";
        String content = "updateContent";

        //when
        ExtractableResponse<Response> response = updateTodo(validToken2, title, content);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("작성자가 다릅니다.");
    }

    @DisplayName("토큰을 가지고 할일의 userId와 동일한 id를 가진 유저는 할일 상태를 변경할 수 있다")
    @Test
    void test7() {
        //given
        ExtractableResponse<Response> postResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(postResponse);

        //when
        ExtractableResponse<Response> response = patchTodoStatus(todoId, validToken1);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("1");
    }

    @DisplayName("토큰을 가졌지만 할일의 userId와 동일하지 않은 id를 가진 유저는 할 일의 상태를 수정 할 수 없다")
    @Test
    void test8() {
        //given
        ExtractableResponse<Response> postResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(postResponse);

        //when
        ExtractableResponse<Response> response = patchTodoStatus(todoId, validToken2);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("작성자가 다릅니다.");
    }

    @DisplayName("토큰을 가지고 할일의 userId와 동일한 id를 가진 유저는 할일을 삭제할 수 있다")
    @Test
    void test9() {
        //given
        ExtractableResponse<Response> postResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(postResponse);

        //when
        ExtractableResponse<Response> response = deleteTodo(todoId, validToken1);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains(todoId + "");
    }

    @DisplayName("토큰을 가졌지만 할일의 userId와 동일하지 않은 id를 가진 유저는 할 일을 삭제할 수 없다")
    @Test
    void test10() {
        //given
        ExtractableResponse<Response> postResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(postResponse);

        //when
        ExtractableResponse<Response> response = deleteTodo(todoId, validToken2);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("사용자는 완료되지 않은 할일만 조회할 수 있다.")
    @Test
    void test11() {
        //given
        postTodo(postInfo(), validToken1);
        ExtractableResponse<Response> postResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(postResponse);

        patchTodoStatus(todoId, validToken1);

        //when
        ExtractableResponse<Response> response = getActiveTodo(validToken1);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> list = response.jsonPath().getList("data");
        assertThat(list.size()).isEqualTo(1);
    }

    @DisplayName("사용자는 자신이 작성한 글만 조회할 수 있다.")
    @Test
    void test12() {
        //given
        postTodo(postInfo(), validToken1);
        postTodo(postInfo(), validToken2);

        //when
        ExtractableResponse<Response> response = getMyTodo(userId, validToken1);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> list = response.jsonPath().getList("data");
        assertThat(list.size()).isEqualTo(1);
    }

    @DisplayName("사용자는 제목으로 할일을 검색할 수 있다")
    @Test
    void test13() {
        //given
        postTodo(postInfo(), validToken1);
        postTodo(postInfo2(), validToken1);
        String title = "anotherTitle";

        //when
        ExtractableResponse<Response> response = searchTodo(title, validToken1);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> list = response.jsonPath().getList("data");
        assertThat(list.size()).isEqualTo(1);
    }

    @AfterEach
    void clear() {
        todoRepository.deleteAll();
    }

    private ExtractableResponse<Response> getAllTodo() {
        return RestAssured.given().log().all()
            .when().get("/api/todos")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getTodo(Long todoId) {
        return RestAssured.given().log().all()
            .when().get("/api/todos/{todoId}", todoId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> postTodo(Map<String, Object> bodyMap,
        String accessToken) {
        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(bodyMap)
            .when().post("/api/todos")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> updateTodo(String accessToken, String updateTitle,
        String updateContent) {
        TodoUpdateRequestDto requestDto = TodoUpdateRequestDto.builder()
            .title(updateTitle)
            .content(updateContent)
            .build();

        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestDto)
            .when().patch("/api/todos/1")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> patchTodoStatus(Long todoId, String accessToken) {
        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .when().patch("/api/todos/{todoId}/status", todoId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> deleteTodo(Long todoId, String accessToken) {
        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .when().delete("/api/todos/{todoId}", todoId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getActiveTodo(String accessToken) {
        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .when().get("/api/todos/filter?completed=true")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getMyTodo(Long userId, String accessToken) {
        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .when().get("/api/todos/filter?userId={userId}", userId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> searchTodo(String title, String accessToken) {
        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .when().get("/api/todos/filter?title={title}", title)
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
        RestAssured.given().log().all()
            .body(requestDto)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/auth/signup")
            .then().log().all()
            .extract();
    }

    private Map<String, Object> postInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "hiyen");
        map.put("title", "myTitle");
        map.put("content", "myContent");
        return map;
    }

    private Map<String, Object> postInfo2() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "hiyen");
        map.put("title", "anotherTitle");
        map.put("content", "anotherContent");
        return map;
    }

    private Long extractIdFromToken(String token) {
        String substring = token.substring(7);
        return jwtUtil.getUserIdFromToken(substring);
    }

    private Long extractIdFromResponse(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("data.id");
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
