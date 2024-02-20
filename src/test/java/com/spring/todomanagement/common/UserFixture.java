package com.spring.todomanagement.common;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.todo_mangement.domain.User;

public interface UserFixture {

    String ANOTHER_PREFIX = "another-";
    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "12345678";

    User TEST_USER = User.builder()
        .name(TEST_USER_NAME)
        .password(TEST_USER_PASSWORD)
        .build();

    UserDto TEST_USER_DTO = UserDto.builder()
        .user(TEST_USER)
        .build();

    User TEST_ANOTHER_USER = User.builder()
        .name(ANOTHER_PREFIX + TEST_USER_NAME)
        .password(TEST_USER_PASSWORD)
        .build();
}
