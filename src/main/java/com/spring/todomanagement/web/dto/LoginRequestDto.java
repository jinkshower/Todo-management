package com.spring.todomanagement.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String password;
}
