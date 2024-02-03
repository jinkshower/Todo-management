package com.spring.todomanagement.web.dto;

import com.spring.todomanagement.domain.comment.Comment;
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
