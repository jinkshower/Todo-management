package com.spring.todomanagement.todo_mangement.domain;

import com.spring.todomanagement.todo_mangement.dto.TodoUpdateRequestDto;
import com.spring.todomanagement.todo_mangement.exception.InvalidUserException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
@NoArgsConstructor
@Table(name = "todos")
@Entity
public class Todo extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus todoStatus = TodoStatus.NOT_DONE;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "todo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Todo(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTodo(this);
    }

    public void update(User user, TodoUpdateRequestDto requestDto) {
        validate(user);
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void changeStatus(User user) {
        validate(user);
        if (this.todoStatus == TodoStatus.DONE) {
            this.todoStatus = TodoStatus.NOT_DONE;
        } else {
            this.todoStatus = TodoStatus.DONE;
        }
    }

    private void validate(User user) {
            if (!Objects.equals(this.user.getId(), user.getId())) {
                String errorMessage = "작성자가 다릅니다. 할일 작성자 ID: " + this.user.getId()
                        + ", 요청 사용자 ID: " + user.getId();
                log.error(errorMessage);
                throw new InvalidUserException(errorMessage);
            }
    }
}