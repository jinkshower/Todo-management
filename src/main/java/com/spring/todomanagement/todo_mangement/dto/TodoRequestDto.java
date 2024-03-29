package com.spring.todomanagement.todo_mangement.dto;

import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotNull
    private String content;

    public Todo toEntity(User user) {
        return Todo.builder()
            .title(title)
            .content(content)
            .user(user)
            .likeCount(0L)
            .build();
    }
}
