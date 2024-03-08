package com.spring.todomanagement.todo_mangement.repository;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.TodoSearchFilter;
import java.util.List;

public interface TodoQueryRepository {

    List<Todo> findAllByOrderByCreatedAtDesc();

    List<Todo> searchByFilter(TodoSearchFilter todoSearchFilter);
}
