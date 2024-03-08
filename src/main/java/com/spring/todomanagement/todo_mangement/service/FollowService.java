package com.spring.todomanagement.todo_mangement.service;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.todo_mangement.dto.FollowResponseDto;

public interface FollowService {

    FollowResponseDto createFollow(UserDto userDto, Long toUserId);
}
