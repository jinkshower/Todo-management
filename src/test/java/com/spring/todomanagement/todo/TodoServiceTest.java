package com.spring.todomanagement.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.spring.todomanagement.common.TodoFixture;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.dto.TodoRequestDto;
import com.spring.todomanagement.todo_mangement.dto.TodoResponseDto;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.service.implementation.TodoServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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

    @DisplayName("할일 조회")
    @Test
    void test2() {
        //given
        Todo testTodo = TEST_TODO;
        given(todoRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(testTodo));

        //when
        TodoResponseDto actual = todoService.getTodo(TEST_TODO_ID);

        //then
        TodoResponseDto expected = new TodoResponseDto(testTodo);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("할일 전체 조회")
    @Test
    void test3() {
        //given
        Todo testTodo = TEST_TODO;
        Todo testTodo2 = TEST_ANOTHER_TODO;
        given(todoRepository.findAllByOrderByCreatedAtDesc())
            .willReturn(List.of(testTodo, testTodo2));

        //when
        List<TodoResponseDto> actual = todoService.getAllTodos();

        //then
        List<TodoResponseDto> expected = Stream.of(testTodo, testTodo2)
            .map(TodoResponseDto::new).toList();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("할일 수정")
    @Test
    void test4() {
        //given
        Todo testTodo = TEST_TODO;
        System.out.println("testTodo.getTitle() = " + testTodo.getTitle());
        given(todoRepository.findById(eq(TEST_USER_ID))).willReturn(Optional.of(testTodo));

        //when
        TodoRequestDto request = TodoRequestDto.builder()
            .title("updateTitle")
            .content("updateContent")
            .build();
        TodoResponseDto actual = todoService.updateTodo(TEST_TODO_ID, TEST_USER_DTO,
            request);

        //then
        assertThat(actual).isEqualTo(new TodoResponseDto(testTodo));
    }
}
