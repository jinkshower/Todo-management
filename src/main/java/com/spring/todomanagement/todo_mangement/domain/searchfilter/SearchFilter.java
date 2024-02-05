package com.spring.todomanagement.todo_mangement.domain.searchfilter;

import com.spring.todomanagement.todo_mangement.domain.Todo;

import java.util.List;

public interface SearchFilter {

    Boolean supports(Object object);
    List<Todo> apply(List<Todo> todos, Object object);
}
