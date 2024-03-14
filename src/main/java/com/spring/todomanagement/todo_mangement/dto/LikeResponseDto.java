package com.spring.todomanagement.todo_mangement.dto;

import com.spring.todomanagement.todo_mangement.domain.Like;
import lombok.Getter;

@Getter
public class LikeResponseDto {

    private final Long id;
    private final Long fromUserId;
    private final Long todoId;

    public LikeResponseDto(Like entity) {
        this.id = entity.getId();
        this.fromUserId = entity.getUserId();
        this.todoId = entity.getTodoId();
    }
}
