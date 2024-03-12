package com.spring.todomanagement.todo_mangement.service.implementation;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.TodoSearchFilter;
import com.spring.todomanagement.todo_mangement.dto.PageDto;
import com.spring.todomanagement.todo_mangement.dto.TodoRequestDto;
import com.spring.todomanagement.todo_mangement.dto.TodoResponseDto;
import com.spring.todomanagement.todo_mangement.exception.InvalidTodoException;
import com.spring.todomanagement.todo_mangement.exception.InvalidUserException;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import com.spring.todomanagement.todo_mangement.service.TodoService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public TodoResponseDto saveTodo(UserDto userDto, TodoRequestDto requestDto) {
        User user = findUser(userDto.getUserId());
        Todo entity = requestDto.toEntity(user);
        todoRepository.save(entity);
        return new TodoResponseDto(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TodoResponseDto> getAllTodos(PageDto pageDto) {
        Page<Todo> todos = todoRepository.findAllByOrderByCreatedAtDesc(pageDto.toPageable());
        return todos.stream().map(TodoResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public TodoResponseDto getTodo(Long todoId) {
        Todo found = findTodo(todoId);
        return new TodoResponseDto(found);
    }

    @Transactional
    @Override
    public TodoResponseDto updateTodo(Long todoId, UserDto userDto, TodoRequestDto requestDto) {
        User user = findUser(userDto.getUserId());
        Todo todo = findTodo(todoId);

        todo.update(user, requestDto);
        return new TodoResponseDto(todo);
    }

    @Transactional
    @Override
    public Long changeTodoStatus(Long todoId, UserDto userDto) {
        User user = findUser(userDto.getUserId());
        Todo todo = findTodo(todoId);

        todo.changeStatus(user.getId());
        return todo.getId();
    }

    @Transactional
    @Override
    public Long deleteTodo(Long todoId, UserDto userDto) {
        User user = findUser(userDto.getUserId());
        Todo todo = findTodo(todoId);

        validateUserId(todo.getUser().getId(), user.getId());

        todoRepository.delete(todo);
        return todo.getId();
    }

    @Override
    public List<TodoResponseDto> searchTodos(TodoSearchFilter searchFilter) {
        return todoRepository.searchByFilter(searchFilter).stream()
            .map(TodoResponseDto::new)
            .toList();
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> {
                String errorMessage = String.format("없는 유저입니다. 요청 ID: " + userId);
                log.error(errorMessage);
                return new InvalidUserException(errorMessage);
            }
        );
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

    private void validateUserId(Long origin, Long input) {
        if (!Objects.equals(origin, input)) {
            String errorMessage = String.format("유저 id 불일치. 유저 ID: %s, 요청 사용자 ID: %s", origin,
                input);
            log.error(errorMessage);
            throw new InvalidUserException(errorMessage);
        }
    }
}
