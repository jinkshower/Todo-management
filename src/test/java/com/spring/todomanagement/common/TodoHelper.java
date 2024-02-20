package com.spring.todomanagement.common;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.User;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class TodoHelper {

    public static Todo get(Todo todo, User user) {
        return get(todo, 1L, LocalDateTime.now(), user);
    }

    public static Todo get(Todo todo, Long id, LocalDateTime createdAt, User user) {
        Todo newTodo = Todo.builder()
            .title(todo.getTitle())
            .content(todo.getContent())
            .build();
        ReflectionTestUtils.setField(newTodo, Todo.class, "id", id, Long.class);
        ReflectionTestUtils.setField(newTodo, Todo.class, "createdAt", createdAt,
            LocalDateTime.class);
        newTodo.setUser(user);
        return newTodo;
    }
}
