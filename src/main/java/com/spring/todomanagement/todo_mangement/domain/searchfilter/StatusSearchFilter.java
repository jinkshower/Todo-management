package com.spring.todomanagement.todo_mangement.domain.searchfilter;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.TodoStatus;

import java.util.List;
import java.util.stream.Collectors;

public class StatusSearchFilter implements SearchFilter {

    @Override
    public Boolean supports(Object object) {
        return object instanceof Boolean;
    }

    @Override
    public List<Todo> apply(List<Todo> todos, Object object) {
        Boolean status = (Boolean) object;
        return todos.stream()
                .filter(todo -> {
                    TodoStatus todoStatus = todo.getTodoStatus();
                    if (status) {
                        return todoStatus == TodoStatus.NOT_DONE;
                    } 
                    return true;
                })
                .collect(Collectors.toList());
    }
}
