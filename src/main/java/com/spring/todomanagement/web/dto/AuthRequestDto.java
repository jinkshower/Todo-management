package com.spring.todomanagement.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthRequestDto {
    private String name;
    private String password;
}
