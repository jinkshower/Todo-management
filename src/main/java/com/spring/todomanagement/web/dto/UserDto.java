package com.spring.todomanagement.web.dto;

import com.spring.todomanagement.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDto {
    private final User user;
}
