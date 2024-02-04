package com.spring.todomanagement.todo_mangement.dto;

import com.spring.todomanagement.todo_mangement.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final Long todoId;
    private final Long userId;
    private final String content;

    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.todoId = entity.getTodo().getId();
        this.userId = entity.getUser().getId();
        this.content = entity.getContent();
    }
}
