package com.spring.todomanagement.todo_mangement.repository;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByOrderByCreatedAtDesc();

    @Transactional
    @Modifying
    @Query(value = "delete from todos", nativeQuery = true)
    void truncate();
}
