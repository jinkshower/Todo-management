package com.spring.todomanagement.todo_mangement.dto;

import com.spring.todomanagement.todo_mangement.domain.Follow;
import lombok.Getter;

@Getter
public class FollowResponseDto {

    private final Long id;
    private final Long fromUserId;
    private final Long toUserId;

    public FollowResponseDto(Follow entity) {
        this.id = entity.getId();
        this.fromUserId = entity.getFollower().getId();
        this.toUserId = entity.getFollowing().getId();
    }
}
