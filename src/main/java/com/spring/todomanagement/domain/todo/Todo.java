package com.spring.todomanagement.domain.todo;

import com.spring.todomanagement.domain.user.User;
import com.spring.todomanagement.web.dto.TodoUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @Builder
    public Todo(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void update(User user, TodoUpdateRequestDto requestDto) {
        validate(user);
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    private void validate(User user) {
        if (!Objects.equals(this.user.getId(), user.getId())) {
            throw new IllegalArgumentException("올바른 유저가 아닙니다");
        }
    }


    public void done() {
        todoStatus = TodoStatus.DONE;
    }

    public void undone() {
        todoStatus = TodoStatus.NOT_DONE;
    }

}
