package com.spring.todomanagement.todo_mangement.repository;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
