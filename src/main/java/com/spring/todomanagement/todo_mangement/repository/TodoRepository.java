package com.spring.todomanagement.todo_mangement.repository;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoQueryRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Todo t where t.id = :id")
    Optional<Todo> findByIdForUpdate(@Param("id") Long id);
}
