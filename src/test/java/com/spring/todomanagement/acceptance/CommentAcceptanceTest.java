package com.spring.todomanagement.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.spring.todomanagement.auth.dto.LoginRequestDto;
import com.spring.todomanagement.auth.dto.SignupRequestDto;
import com.spring.todomanagement.auth.support.JwtUtil;
import com.spring.todomanagement.todo_mangement.domain.Comment;
import com.spring.todomanagement.todo_mangement.dto.CommentRequestDto;
import com.spring.todomanagement.todo_mangement.repository.CommentRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CommentAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CommentRepository commentRepository;

    private Long userId;
    private String validToken1;
    private String validToken2;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        TokenResponse tokenResponse1 = getValidUserInfo("hiyen", "12345678");
        TokenResponse tokenResponse2 = getValidUserInfo("jackie", "87654321");
        userId = tokenResponse1.getUserId();
        validToken1 = tokenResponse1.getToken();
        validToken2 = tokenResponse2.getToken();
    }

    @DisplayName("토큰을 가진 유저는 할일에 댓글을 달 수 있다")
    @Test
    void test1() {
        //given
        ExtractableResponse<Response> todoResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(todoResponse);

        //when
        ExtractableResponse<Response> response = postComment(todoId, validToken1);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        List<Comment> foundTodos = commentRepository.findAllByTodoId(todoId);
        assertThat(foundTodos.get(0).getUser().getId()).isEqualTo(userId);
    }

    @DisplayName("토큰이 없는 유저는 할일에 댓글을 달 수 없다")
    @Test
    void test2() {
        //given
        ExtractableResponse<Response> todoResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(todoResponse);

        //when
        ExtractableResponse<Response> response = postComment(todoId, "1234");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("토큰이 있고 해당 작성자가 작성한 댓글을 수정할 수 있다")
    @Test
    void test3() {
        //given
        ExtractableResponse<Response> todoResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(todoResponse);
        ExtractableResponse<Response> commentResponse = postComment(todoId, validToken1);
        Long commentId = extractIdFromResponse(commentResponse);

        String updateContent = "updateContent";

        //when
        ExtractableResponse<Response> response = updateComment(updateContent, validToken1, todoId,
            commentId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("updateContent");
    }

    @DisplayName("토큰이 있지만 해당 작성자가 아니면 댓글을 수정할 수 없다")
    @Test
    void test4() {
        //given
        ExtractableResponse<Response> todoResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(todoResponse);
        ExtractableResponse<Response> commentResponse = postComment(todoId, validToken1);
        Long commentId = extractIdFromResponse(commentResponse);

        String updateContent = "updateContent";

        //when
        ExtractableResponse<Response> response = updateComment(updateContent, validToken2, todoId,
            commentId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("작성자가 다릅니다.");
    }

    @DisplayName("토큰이 있고 해당 작성자가 작성한 댓글을 삭제할 수 있다")
    @Test
    void test5() {
        //given
        ExtractableResponse<Response> todoResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(todoResponse);
        ExtractableResponse<Response> commentResponse = postComment(todoId, validToken1);
        Long commentId = extractIdFromResponse(commentResponse);

        //when
        ExtractableResponse<Response> response = deleteComment(validToken1, todoId, commentId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains(commentId + "");
    }

    @DisplayName("토큰이 있지만 해당 작성자가 아니면 댓글을 삭제할 수 없다")
    @Test
    void test6() {
        //given
        ExtractableResponse<Response> todoResponse = postTodo(postInfo(), validToken1);
        Long todoId = extractIdFromResponse(todoResponse);
        ExtractableResponse<Response> commentResponse = postComment(todoId, validToken1);
        Long commentId = extractIdFromResponse(commentResponse);

        //when
        ExtractableResponse<Response> response = deleteComment(validToken2, todoId, commentId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("댓글 삭제 실패");
    }

    private ExtractableResponse<Response> postComment(Long todoId, String accessToken) {
        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(commentMap())
            .when().post("/api/todos/{todoId}/comments", todoId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> updateComment(String content,
        String accessToken,
        Long todoId,
        Long commentId) {
        CommentRequestDto requestDto = CommentRequestDto.builder()
            .content(content).build();

        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestDto)
            .when().patch("/api/todos/{todoId}/comments/{commentId}", todoId, commentId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> deleteComment(String accessToken, Long todoId,
        Long commentId) {
        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .when().delete("/api/todos/{todoId}/comments/{commentId}", todoId, commentId)
            .then().log().all()
            .extract();
    }

    private Map<String, Object> commentMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("content", "myCommentContent");
        return map;
    }

    private ExtractableResponse<Response> postTodo(Map<String, Object> postInfo,
        String accessToken) {
        return RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(postInfo)
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
