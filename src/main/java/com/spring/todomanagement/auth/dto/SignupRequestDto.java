package com.spring.todomanagement.auth.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequestDto {
    @Size(min = 4, max = 10, message = "이름은 최소 4자, 최대 10자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "이름은 소문자, 숫자만 가능합니다.")
    private String name;
    @Size(min = 8, max = 15, message = "비밀번호는 최소8자, 최대 15자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "대소문자, 숫자만 가능합니다.")
    private String password;
}
