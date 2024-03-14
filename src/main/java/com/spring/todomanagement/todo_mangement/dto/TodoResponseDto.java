package com.spring.todomanagement.todo_mangement.dto;

import com.spring.todomanagement.todo_mangement.domain.Comment;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.TodoStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class TodoResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private TodoStatus status;
    private LocalDateTime createdAt;
    private Long likeCount;

    private final List<CommentResponseDto> comments = new ArrayList<>();

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.author = todo.getUser().getName();
        this.status = todo.getTodoStatus();
        this.createdAt = todo.getCreatedAt();
        this.likeCount = todo.getLikeCount();

        for (Comment comment : todo.getComments()) {
            comments.add(new CommentResponseDto(comment));
        }
    }
}
