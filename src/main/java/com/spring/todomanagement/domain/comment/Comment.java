package com.spring.todomanagement.domain.comment;

import com.spring.todomanagement.domain.todo.Todo;
import com.spring.todomanagement.domain.user.User;
import com.spring.todomanagement.web.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
            throw new IllegalArgumentException("작성자가 같아야 합니다.");
        }
        if (!this.todo.equals(todo)) {
            throw new IllegalArgumentException("다른 할일에 있는 댓글입니다.");
        }
    }
}
