package com.sparta.secureschedulerappserver.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.GroupSequence;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @GroupSequence({Default.class, BlankCheck.class, SizeCheck.class, PatternCheck.class})
    interface FullValidation extends Default {}

    interface BlankCheck {}
    interface SizeCheck {}
    interface PatternCheck {}

    static class UserRequestDto {
        @NotBlank(groups = BlankCheck.class, message = "유저네임을 입력하세요.")
        @Size(groups = SizeCheck.class, min = 4, max = 10, message = "4자 이상 10자 이하로 입력하세요.")
        @Pattern(groups = PatternCheck.class, regexp = "^[a-z0-9]+$", message = "알파벳 소문자, 숫자로만 구성되어야 합니다.")
        private String username;

        @NotBlank(groups = BlankCheck.class, message = "패스워드를 입력하세요.")
        @Size(groups = SizeCheck.class, min = 8, max = 15, message = "8자 이상 15자 이하로 입력하세요.")
        @Pattern(groups = PatternCheck.class, regexp = "^[a-z0-9]+$", message = "알파벳 소문자, 숫자로만 구성되어야 합니다.")
        private String password;

        public UserRequestDto(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
    @Test
    @DisplayName("유효한 값 입력 테스트")
    void validValuesValidation() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("username", "password123");

        // when
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto, FullValidation.class);

        // then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("빈 값 입력 테스트")
    void blankValuesValidation() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("", "");

        // when
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto, BlankCheck.class);

        // then
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("유저네임을 입력하세요.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("패스워드를 입력하세요.")));
    }


    @Test
    @DisplayName("사이즈 유효성 검사 테스트")
    void sizeValidation() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("us", "password123");

        // when
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto, SizeCheck.class);

        // then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("4자 이상 10자 이하로 입력하세요.")));
    }

    @Test
    @DisplayName("패턴 유효성 검사 테스트")
    void patternValidation() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("username123", "pass@word");

        // when
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto, PatternCheck.class);

        // then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("알파벳 소문자, 숫자로만 구성되어야 합니다.")));
    }

}