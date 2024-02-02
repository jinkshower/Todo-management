package com.spring.todomanagement.service.todo;

import com.spring.todomanagement.domain.todo.Todo;
import com.spring.todomanagement.domain.todo.TodoRepository;
import com.spring.todomanagement.domain.user.User;
import com.spring.todomanagement.web.dto.TodoResponseDto;
import com.spring.todomanagement.web.dto.TodoSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
