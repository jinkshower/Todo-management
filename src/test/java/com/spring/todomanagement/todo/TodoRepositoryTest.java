package com.spring.todomanagement.todo;

import static org.assertj.core.api.Assertions.assertThat;

import com.spring.todomanagement.common.TodoFixture;
import com.spring.todomanagement.common.TodoHelper;
import com.spring.todomanagement.common.util.DatabaseSupporter;
import com.spring.todomanagement.todo_mangement.domain.Timestamped;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.TodoStatus;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.TodoSearchFilter;
import com.spring.todomanagement.todo_mangement.repository.TodoQueryRepository;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TodoRepositoryTest extends DatabaseSupporter implements TodoFixture {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    TodoQueryRepository todoQueryRepository;

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
        List<Todo> actual = todoQueryRepository.findAllByOrderByCreatedAtDesc();

        //then
        List<LocalDateTime> times = actual.stream()
            .map(Timestamped::getCreatedAt)
            .toList();
        assertThat(times.get(2)).isBefore(times.get(1));
        assertThat(times.get(1)).isBefore(times.get(0));
    }

    @Nested
    @DisplayName("검색 조건 테스트")
    class searchFilter {

        @DisplayName("검색 조건 테스트 기본(완료되지 않은 할일)")
        @Test
        void test2() {
            //given
            Todo testTodo1 = TodoHelper.get(TEST_TODO, 1L, LocalDateTime.now(), TEST_USER);
            Todo testTodo2 = TodoHelper.get(TEST_TODO, 2L, LocalDateTime.now(), TEST_USER);
            testTodo1.changeStatus(TEST_USER_ID);
            todoRepository.save(testTodo1);
            todoRepository.save(testTodo2);
            TodoSearchFilter searchFilter = TodoSearchFilter.builder().build();

            //when
            List<Todo> todos = todoQueryRepository.searchByFilter(searchFilter);

            //then
            assertThat(todos.size()).isEqualTo(1);
            assertThat(todos).doesNotContain(testTodo2);
        }

        @DisplayName("검색 조건 테스트 작성자로 검색")
        @Test
        void test3() {
            //given
            Todo testTodo1 = TodoHelper.get(TEST_TODO, 1L, LocalDateTime.now(), TEST_USER);
            Todo testTodo2 = TodoHelper.get(TEST_TODO, 2L, LocalDateTime.now(), TEST_ANOTHER_USER);
            userRepository.save(TEST_ANOTHER_USER);
            todoRepository.save(testTodo1);
            todoRepository.save(testTodo2);
            TodoSearchFilter searchFilter = TodoSearchFilter.builder()
                .userId(TEST_USER_ID)
                .build();

            //when
            List<Todo> todos = todoQueryRepository.searchByFilter(searchFilter);

            //then
            assertThat(todos.size()).isEqualTo(1);
            assertThat(todos).doesNotContain(testTodo2);
        }

        @DisplayName("검색 조건 테스트 제목으로 검색")
        @Test
        void test4() {
            //given
            Todo testTodo1 = TodoHelper.get(TEST_TODO, 1L, LocalDateTime.now(), TEST_USER);
            Todo testTodo2 = TodoHelper.get(TEST_ANOTHER_TODO, 2L, LocalDateTime.now(),
                TEST_ANOTHER_USER);
            userRepository.save(TEST_ANOTHER_USER);
            todoRepository.save(testTodo1);
            todoRepository.save(testTodo2);
            TodoSearchFilter searchFilter = TodoSearchFilter.builder()
                .title(testTodo1.getTitle())
                .build();

            //when
            List<Todo> todos = todoQueryRepository.searchByFilter(searchFilter);

            //then
            assertThat(todos.size()).isEqualTo(1);
            assertThat(todos).doesNotContain(testTodo2);
        }

        @DisplayName("검색 조건 테스트 제목 작성자 완료여부 검색")
        @Test
        void test5() {
            //given
            Todo testTodo1 = TodoHelper.get(TEST_TODO, 1L, LocalDateTime.now(), TEST_USER);
            Todo testTodo2 = TodoHelper.get(TEST_ANOTHER_TODO, 2L, LocalDateTime.now(),
                TEST_ANOTHER_USER);
            userRepository.save(TEST_ANOTHER_USER);
            todoRepository.save(testTodo1);
            todoRepository.save(testTodo2);
            TodoSearchFilter searchFilter = TodoSearchFilter.builder()
                .userId(TEST_USER_ID)
                .title(testTodo1.getTitle())
                .todoStatus(TodoStatus.NOT_DONE)
                .build();

            //when
            List<Todo> todos = todoQueryRepository.searchByFilter(searchFilter);

            //then
            assertThat(todos.size()).isEqualTo(1);
            assertThat(todos).doesNotContain(testTodo2);
        }
    }
}
