package com.spring.todomanagement.like;

import com.spring.todomanagement.common.TodoFixture;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.repository.LikeRepository;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import com.spring.todomanagement.todo_mangement.service.LikeService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
public class LikeTest implements TodoFixture {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    LikeService likeService;

    @Test
    void concurrency_test() throws InterruptedException {
        //given
        User user = TEST_USER;
        userRepository.save(user);
        Todo todo = TEST_TODO;
        todoRepository.save(todo);

        int testCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(testCount);
        CountDownLatch latch = new CountDownLatch(testCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        //when
        for (int i = 0; i < testCount; i++) {
            int count = i;
            executorService.submit(() -> {
                try {
                    likeService.createLike((long) count + 1L, 1L);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        //then
        Assertions.assertThat(likeRepository.count()).isEqualTo(testCount);
    }
}
