package com.spring.todomanagement.todo;

import static org.assertj.core.api.Assertions.assertThat;

import com.spring.todomanagement.common.TodoFixture;
import com.spring.todomanagement.common.TodoHelper;
import com.spring.todomanagement.todo_mangement.domain.Timestamped;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class TodoRepositoryTest implements TodoFixture {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(TEST_USER);
    }

    @DisplayName("작성일 내림차순 정렬 조회")
    @Test
    void test1() {
        //given
        Todo testTodo1 =
            TodoHelper.get(TEST_TODO, 1L, LocalDateTime.now().minusMinutes(2), TEST_USER);
        Todo testTodo2 =
            TodoHelper.get(TEST_TODO, 2L, LocalDateTime.now().minusMinutes(1), TEST_USER);
        Todo testTodo3 =
            TodoHelper.get(TEST_TODO, 3L, LocalDateTime.now(), TEST_USER);
        todoRepository.save(testTodo1);
        todoRepository.save(testTodo2);
        todoRepository.save(testTodo3);

        //when
        List<Todo> actual = todoRepository.findAllByOrderByCreatedAtDesc();

        //then
        List<LocalDateTime> times = actual.stream()
            .map(Timestamped::getCreatedAt)
            .toList();
        assertThat(times.get(2)).isBefore(times.get(1));
        assertThat(times.get(1)).isBefore(times.get(0));
    }
}
