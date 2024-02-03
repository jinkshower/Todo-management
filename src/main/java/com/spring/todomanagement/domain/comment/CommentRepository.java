package com.spring.todomanagement.domain.comment;

import com.spring.todomanagement.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTodoId(Long todoId);
}
