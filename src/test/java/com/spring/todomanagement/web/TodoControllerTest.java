package com.spring.todomanagement.web;

import com.spring.todomanagement.auth.JwtUtil;
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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
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
        //given
        String token = getValidToken();

        //when
        ExtractableResponse<Response> response = requestTodoPost(bodyMap(), token);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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

    private String getValidToken() {
        return jwtUtil.createToken("hiyen");
    }


    private Map<String, Object> bodyMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "hiyen");
        map.put("title", "myTitle");
        map.put("content", "myContent");
        return map;
    }
}