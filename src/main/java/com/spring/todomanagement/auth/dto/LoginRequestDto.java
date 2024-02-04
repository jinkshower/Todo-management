package com.spring.todomanagement.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotBlank(message = "패스워드는 필수입니다.")
    private String password;
}
