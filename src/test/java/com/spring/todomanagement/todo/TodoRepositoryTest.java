package com.spring.todomanagement.todo;

import static org.assertj.core.api.Assertions.assertThat;

import com.spring.todomanagement.common.TodoFixture;
import com.spring.todomanagement.common.TodoHelper;
import com.spring.todomanagement.common.util.DatabaseSupporter;
import com.spring.todomanagement.todo_mangement.domain.Timestamped;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.TodoStatus;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.TodoSearchFilter;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
//@DataJpaTest
//@Import(DataConfig.class)
public class TodoRepositoryTest extends DatabaseSupporter implements TodoFixture {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(TEST_USER);
        userRepository.save(TEST_ANOTHER_USER);
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
        Page<Todo> actual = todoRepository.findAllByOrderByCreatedAtDesc(PAGE_DTO.toPageable());

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
            List<Todo> todos = todoRepository.searchByFilter(searchFilter);

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
//            userRepository.save(TEST_ANOTHER_USER);
            todoRepository.save(testTodo1);
            todoRepository.save(testTodo2);
            TodoSearchFilter searchFilter = TodoSearchFilter.builder()
                .userId(TEST_USER_ID)
                .build();

            //when
            List<Todo> todos = todoRepository.searchByFilter(searchFilter);

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
//            userRepository.save(TEST_ANOTHER_USER);
            todoRepository.save(testTodo1);
            todoRepository.save(testTodo2);
            TodoSearchFilter searchFilter = TodoSearchFilter.builder()
                .title(testTodo1.getTitle())
                .build();

            //when
            List<Todo> todos = todoRepository.searchByFilter(searchFilter);

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
//            userRepository.save(TEST_ANOTHER_USER);
            todoRepository.save(testTodo1);
            todoRepository.save(testTodo2);
            TodoSearchFilter searchFilter = TodoSearchFilter.builder()
                .userId(TEST_USER_ID)
                .title(testTodo1.getTitle())
                .todoStatus(TodoStatus.NOT_DONE)
                .build();

            //when
            List<Todo> todos = todoRepository.searchByFilter(searchFilter);

            //then
            assertThat(todos.size()).isEqualTo(1);
            assertThat(todos).doesNotContain(testTodo2);
        }
    }

    @Test
    @Disabled
    void pageTest() {
        for (int i = 0; i < 100; i++) {
            todoRepository.save(Todo.builder()
                .title(TEST_TODO_TITLE)
                .content(TEST_TODO_CONTENT + 1)
                .user(TEST_USER)
                .build());
        }

        System.out.println(todoRepository.findAll().size());
        Page<Todo> found = todoRepository.findAllByOrderByCreatedAtDesc(
            PAGE_DTO.toPageable());
        Pageable pageable = found.getPageable();
        Sort sort = pageable.getSort();
        // 페이지 정보
        System.out.println("Sort (Sorted): " + sort.isSorted());
        System.out.println("Sort (Unsorted): " + sort.isUnsorted());
        System.out.println("Sort (Empty): " + sort.isEmpty());
        System.out.println("Page Size: " + pageable.getPageSize());
        System.out.println("Page Number: " + pageable.getPageNumber());
        System.out.println("Offset: " + pageable.getOffset());
        System.out.println("Is Paged: " + pageable.isPaged());
        System.out.println("Is Unpaged: " + pageable.isUnpaged());

// 전체 페이지 정보
        System.out.println("Total Pages: " + found.getTotalPages());
        System.out.println("Total Elements: " + found.getTotalElements());
        System.out.println("Is Last Page: " + found.isLast());
        System.out.println("Current Page Number: " + found.getNumber() + 1);
        System.out.println("Is First Page: " + found.isFirst());
        System.out.println("Is Empty: " + found.isEmpty());
        System.out.println("Size: " + found.getSize());
        System.out.println("Number Of Elements: " + found.getNumberOfElements());
    }
}
