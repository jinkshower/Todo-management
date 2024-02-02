package com.spring.todomanagement.web.dto;

import com.spring.todomanagement.domain.todo.Todo;
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
}