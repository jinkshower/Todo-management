package com.spring.todomanagement.todo_mangement.service;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.todo_mangement.dto.TodoRequestDto;
import com.spring.todomanagement.todo_mangement.dto.TodoResponseDto;
import java.util.List;

public interface TodoService {

    TodoResponseDto saveTodo(UserDto userDto, TodoRequestDto requestDto);

    List<TodoResponseDto> getAllTodos();

    TodoResponseDto getTodo(Long todoId);

    TodoResponseDto updateTodo(Long todoId, UserDto userDto, TodoRequestDto requestDto);

    Long changeTodoStatus(Long todoId, UserDto userDto);

    Long deleteTodo(Long todoId, UserDto userDto);

    List<TodoResponseDto> getFilteredTodos(Boolean completed, Long userId, String title,
        UserDto userDto);
}
