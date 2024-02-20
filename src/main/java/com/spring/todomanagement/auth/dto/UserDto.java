package com.spring.todomanagement.auth.dto;

import com.spring.todomanagement.todo_mangement.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class UserDto {

    private final User user;
}
