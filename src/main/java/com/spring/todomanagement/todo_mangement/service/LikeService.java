package com.spring.todomanagement.todo_mangement.service;

import com.spring.todomanagement.todo_mangement.dto.LikeResponseDto;

public interface LikeService {

    LikeResponseDto createLike(Long userId, Long todoId);
}
