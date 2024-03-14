package com.spring.todomanagement.todo_mangement.domain;

import com.spring.todomanagement.todo_mangement.dto.TodoRequestDto;
import com.spring.todomanagement.todo_mangement.exception.InvalidUserException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "todos")
@Entity
public class Todo extends Timestamped implements Serializable {

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

    @Column(nullable = false)
    private Long likeCount;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "todo", cascade = {CascadeType.PERSIST,
        CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Todo(String title, String content, User user, Long likeCount) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.likeCount = likeCount;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTodo(this);
    }

    public void update(User user, TodoRequestDto requestDto) {
        validate(user.getId());
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void changeStatus(Long userId) {
        validate(userId);
        if (this.todoStatus == TodoStatus.DONE) {
            this.todoStatus = TodoStatus.NOT_DONE;
        } else {
            this.todoStatus = TodoStatus.DONE;
        }
    }

    public void incrementLikeCount() {
        this.likeCount += 1L;
    }

    private void validate(Long userId) {
        if (!Objects.equals(this.user.getId(), userId)) {
            String errorMessage = "작성자가 다릅니다. 할일 작성자 ID: " + this.user.getId()
                + ", 요청 사용자 ID: " + userId;
            log.error(errorMessage);
            throw new InvalidUserException(errorMessage);
        }
    }
}
