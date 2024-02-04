package com.spring.todomanagement.todo_mangement.domain;

import com.spring.todomanagement.todo_mangement.dto.CommentRequestDto;
import com.spring.todomanagement.todo_mangement.exception.InvalidTodoException;
import com.spring.todomanagement.todo_mangement.exception.InvalidUserException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@Table(name = "comments")
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    @Builder
    public Comment(String content, User user, Todo todo) {
        this.content = content;
        this.user = user;
        this.todo = todo;
    }

    public void update(User user, Todo todo, CommentRequestDto requestDto) {
        validate(user, todo);
        this.content = requestDto.getContent();
    }

    private void validate(User user, Todo todo) {
        if (!this.user.equals(user)) {
            String errorMessage = "작성자가 다릅니다. 댓글 ID: " + this.id;
            log.error(errorMessage);
            throw new InvalidUserException(errorMessage);

        }
        if (!this.todo.equals(todo)) {
            String errorMessage = "다른 할일에 있는 댓글입니다. 댓글 ID: " + this.id;
            log.error(errorMessage);
            throw new InvalidTodoException(errorMessage);
        }
    }
}
