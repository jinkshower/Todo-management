package com.spring.todomanagement.web.dto;

import com.spring.todomanagement.domain.todo.Todo;
import com.spring.todomanagement.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoSaveRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public Todo toEntity(User user) {
        return Todo.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
