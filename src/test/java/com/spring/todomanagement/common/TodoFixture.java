package com.spring.todomanagement.common;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.dto.TodoRequestDto;
import com.spring.todomanagement.todo_mangement.dto.TodoResponseDto;

public interface TodoFixture extends UserFixture {

    Long TEST_TODO_ID = 1L;
    String TEST_TODO_TITLE = "title";
    String TEST_TODO_CONTENT = "content";

    TodoRequestDto TEST_TODO_REQUEST_DTO = TodoRequestDto.builder()
        .title(TEST_TODO_TITLE)
        .content(TEST_TODO_CONTENT)
        .build();
    Todo TEST_TODO = Todo.builder()
        .title(TEST_TODO_TITLE)
        .content(TEST_TODO_CONTENT)
        .build();
    TodoResponseDto TEST_TODO_RESPONSE_DTO = new TodoResponseDto(TEST_TODO);
    Todo TEST_ANOTHER_TODO = Todo.builder()
        .title(ANOTHER_PREFIX + TEST_TODO_TITLE)
        .content(ANOTHER_PREFIX + TEST_TODO_CONTENT)
        .build();
}
