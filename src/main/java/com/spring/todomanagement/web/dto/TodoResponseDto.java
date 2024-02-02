package com.spring.todomanagement.web.dto;

import com.spring.todomanagement.domain.todo.Todo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.author = todo.getUser().getName();
        this.createdAt = todo.getCreatedAt();
    }
}