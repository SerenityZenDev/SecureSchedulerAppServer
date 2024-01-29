package com.sparta.secureschedulerappserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequestDto {
    @NotBlank
    @Size(min = 4, max = 10, message = "4자 이상 10자 이하로 입력하세요.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "알파벳 소문자, 숫자로만 구성되어야 합니다.")
    private String username;

    @Size(min = 8, max = 15, message = "8자 이상 15자 이하로 입력하세요.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "알파벳 소문자, 숫자로만 구성되어야 합니다.")
    @NotBlank
    private String password;
}
