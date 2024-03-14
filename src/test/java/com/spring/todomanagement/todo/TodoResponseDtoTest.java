package com.spring.todomanagement.todo;

import com.spring.todomanagement.common.TodoFixture;
import com.spring.todomanagement.todo_mangement.domain.Comment;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.repository.CommentRepository;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TodoResponseDtoTest implements TodoFixture {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void test1() {
        User user = TEST_USER;
        Todo todo = Todo.builder()
            .title("title")
            .content("content")
            .user(user)
            .likeCount(0L)
            .build();
        userRepository.save(user);
        todoRepository.save(todo);

        Comment comment = Comment.builder()
            .todo(todo)
            .content("content")
            .user(user)
            .build();
        todo.addComment(comment);
        commentRepository.save(comment);

        List<Comment> comments = todo.getComments();
//        List<CommentResponseDto> comments = responseDto.getComments();
        for (var cor : comments) {
            System.out.println(cor.getTodo().getContent());
        }
    }
}
