package com.spring.todomanagement.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class UserDto {

    //    private final User user;
    private final Long userId;
}
