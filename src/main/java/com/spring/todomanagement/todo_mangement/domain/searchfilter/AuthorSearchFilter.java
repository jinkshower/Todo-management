package com.spring.todomanagement.todo_mangement.domain.searchfilter;

import com.spring.todomanagement.todo_mangement.domain.Todo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AuthorSearchFilter implements SearchFilter {

    @Override
    public Boolean supports(Object object) {
        return object instanceof Long;
    }

    @Override
    public List<Todo> apply(List<Todo> todos, Object object) {
        Long id = (Long) object;
        return todos.stream()
                .filter(todo -> Objects.equals(todo.getUser().getId(), id))
                .collect(Collectors.toList());
    }
}
