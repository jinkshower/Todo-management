package com.spring.todomanagement.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoUpdateRequestDto {
    private String title;
    private String content;
}
