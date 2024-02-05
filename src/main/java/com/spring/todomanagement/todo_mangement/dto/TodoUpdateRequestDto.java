package com.spring.todomanagement.todo_mangement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoUpdateRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
