package com.spring.todomanagement.service.todo;

import com.spring.todomanagement.domain.todo.Todo;
import com.spring.todomanagement.domain.todo.TodoRepository;
import com.spring.todomanagement.domain.user.User;
import com.spring.todomanagement.web.dto.TodoResponseDto;
import com.spring.todomanagement.web.dto.TodoSaveRequestDto;
import com.spring.todomanagement.web.dto.TodoUpdateRequestDto;
import com.spring.todomanagement.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Transactional
    public TodoResponseDto saveTodo(User user, TodoSaveRequestDto requestDto) {
        Todo entity = requestDto.toEntity(user);
        todoRepository.save(entity);
        log.info("저장 되었습니다.");
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
            throw new IllegalArgumentException("작성자만 할일을 삭제할 수 있습니다.");
        }
        todoRepository.delete(todo);
        return todo.getId();
    }

    private Todo findTodo(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalArgumentException("해당 할일이 존재하지 않습니다.")
        );
    }
}
