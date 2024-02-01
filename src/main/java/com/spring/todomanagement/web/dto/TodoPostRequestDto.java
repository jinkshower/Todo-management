package com.spring.todomanagement.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoPostRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
