package com.spring.todomanagement.todo_mangement.service;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.exception.InvalidTodoException;
import com.spring.todomanagement.todo_mangement.exception.InvalidUserException;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.dto.TodoResponseDto;
import com.spring.todomanagement.todo_mangement.dto.TodoSaveRequestDto;
import com.spring.todomanagement.todo_mangement.dto.TodoUpdateRequestDto;
import com.spring.todomanagement.auth.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Transactional
    public TodoResponseDto saveTodo(User user, TodoSaveRequestDto requestDto) {
        Todo entity = requestDto.toEntity(user);
        todoRepository.save(entity);
        return new TodoResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<TodoResponseDto> getAllTodos() {
        return todoRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(TodoResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public TodoResponseDto getTodo(Long todoId) {
        Todo found = findTodo(todoId);
        return new TodoResponseDto(found);
    }

    @Transactional
    public TodoResponseDto updateTodo(Long todoId, UserDto userDto, TodoUpdateRequestDto requestDto) {
        User user = userDto.getUser();
        Todo todo = findTodo(todoId);

        todo.update(user, requestDto);
        return new TodoResponseDto(todo);
    }

    @Transactional
    public Long changeTodoStatus(Long todoId, UserDto userDto) {
        User user = userDto.getUser();
        Todo todo = findTodo(todoId);

        todo.changeStatus(user);
        return todo.getId();
    }

    @Transactional
    public Long deleteTodo(Long todoId, UserDto userDto) {
        User user = userDto.getUser();
        Todo todo = findTodo(todoId);
        if (!Objects.equals(todo.getUser().getId(), user.getId())) {
            String errorMessage = "할일 삭제 실패 - 작성자 id 불일치. 할일 작성자 ID: " + todo.getUser().getId()
                    + ", 요청 사용자 ID: " + user.getId();
            log.error(errorMessage);
            throw new InvalidUserException(errorMessage);
        }
        todoRepository.delete(todo);
        return todo.getId();
    }

    private Todo findTodo(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(
                () -> {
                    String errorMessage = "없는 할일입니다. 요청 ID: " + todoId;
                    log.error(errorMessage);
                    return new InvalidTodoException(errorMessage);
                }
        );
    }
}
