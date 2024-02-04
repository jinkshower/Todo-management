package com.spring.todomanagement.todo_mangement.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoUpdateRequestDto {
    private String title;
    private String content;
}
