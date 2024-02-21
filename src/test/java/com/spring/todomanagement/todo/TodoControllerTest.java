package com.spring.todomanagement.todo;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.auth.support.JwtUtil;
import com.spring.todomanagement.common.ControllerTest;
import com.spring.todomanagement.common.TodoFixture;
import com.spring.todomanagement.todo_mangement.dto.TodoRequestDto;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import com.spring.todomanagement.todo_mangement.service.implementation.TodoServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class TodoControllerTest extends ControllerTest implements TodoFixture {

    @MockBean
    private TodoServiceImpl todoService;

    @MockBean
    private UserRepository userRepository;

    @DisplayName("할일 생성 요청")
    @Test
    void test1() throws Exception {
        //given
        given(userRepository.findById(eq(TEST_USER_ID))).willReturn(Optional.of(TEST_USER));

        //when
        ResultActions action = mockMvc.perform(post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(JwtUtil.AUTHORIZATION_HEADER, token())
            .content(objectMapper.writeValueAsString(TEST_TODO_REQUEST_DTO)));

        //then
        action.andExpect(status().isCreated());
        verify(todoService, times(1))
            .saveTodo(any(UserDto.class), any(TodoRequestDto.class));
    }

    String token() {
        return jwtUtil.createToken(TEST_USER_ID);
    }
}
