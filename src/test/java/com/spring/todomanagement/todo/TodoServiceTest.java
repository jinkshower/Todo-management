package com.spring.todomanagement.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.spring.todomanagement.common.TodoFixture;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.TodoStatus;
import com.spring.todomanagement.todo_mangement.dto.PageDto;
import com.spring.todomanagement.todo_mangement.dto.TodoRequestDto;
import com.spring.todomanagement.todo_mangement.dto.TodoResponseDto;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import com.spring.todomanagement.todo_mangement.service.implementation.TodoServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest implements TodoFixture {

    @InjectMocks
    TodoServiceImpl todoService;

    @Mock
    TodoRepository todoRepository;

    @Mock
    UserRepository userRepository;

    @DisplayName("할일 생성")
    @Test
    void test1() {
        //given
        Todo testTodo = TEST_TODO;
        given(todoRepository.save(any(Todo.class))).willReturn(testTodo);
        given(userRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(TEST_USER));

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
        PageDto pageDto = PageDto.builder()
            .currentPage(1)
            .size(10)
            .sortBy("createdAt")
            .build();
        given(todoRepository.findAllByOrderByCreatedAtDesc(pageDto.toPageable()))
            .willReturn(new PageImpl<>(List.of(testTodo, testTodo2), pageDto.toPageable(), 2));

        //when
        List<TodoResponseDto> actual = todoService.getAllTodos(pageDto);

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
        given(todoRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(testTodo));
        given(userRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(TEST_USER));

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

    @DisplayName("할일 삭제")
    @Test
    void test5() {
        //given
        given(todoRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(TEST_TODO));
        given(userRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(TEST_USER));

        //when
        todoService.deleteTodo(TEST_TODO_ID, TEST_USER_DTO);

        //then
        verify(todoRepository, times(1))
            .delete(ArgumentMatchers.any(Todo.class));
    }

    @DisplayName("할일 완료 처리")
    @Test
    void test6() {
        //given
        Todo testTodo = TEST_TODO;
        given(todoRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(testTodo));
        given(userRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(TEST_USER));

        //when
        todoService.changeTodoStatus(TEST_TODO_ID, TEST_USER_DTO);

        //then
        assertThat(testTodo.getTodoStatus()).isEqualTo(TodoStatus.DONE);
    }
}
