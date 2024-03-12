package com.spring.todomanagement.todo_mangement.repository;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.TodoSearchFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoQueryRepository {

    Page<Todo> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Todo> searchByFilter(TodoSearchFilter todoSearchFilter);
}
