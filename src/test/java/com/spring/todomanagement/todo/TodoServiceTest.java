package com.spring.todomanagement.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.spring.todomanagement.common.TodoFixture;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.dto.TodoResponseDto;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.service.implementation.TodoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest implements TodoFixture {

    @InjectMocks
    TodoServiceImpl todoService;

    @Mock
    TodoRepository todoRepository;

    @DisplayName("할일 생성")
    @Test
    void test1() {
        //given
        Todo testTodo = TEST_TODO;
        given(todoRepository.save(any(Todo.class))).willReturn(testTodo);

        //when
        TodoResponseDto actual =
            todoService.saveTodo(TEST_USER_DTO, TEST_TODO_REQUEST_DTO);

        //then
        TodoResponseDto expected = new TodoResponseDto(testTodo);
        assertThat(actual).isEqualTo(expected);
    }
}
