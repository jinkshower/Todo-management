package com.spring.todomanagement.todo_mangement.domain.searchfilter;

import com.spring.todomanagement.todo_mangement.domain.Todo;

import java.util.List;
import java.util.stream.Collectors;

public class TitleSearchFilter implements SearchFilter {

    @Override
    public Boolean supports(Object object) {
        return object instanceof String;
    }

    @Override
    public List<Todo> apply(List<Todo> todos, Object object) {
        String title = (String) object;
        return todos.stream()
                .filter(todo -> todo.getTitle().equals(title))
                .collect(Collectors.toList());
    }
}
