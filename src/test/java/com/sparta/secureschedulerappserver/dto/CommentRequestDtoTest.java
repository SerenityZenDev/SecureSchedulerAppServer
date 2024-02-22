package com.sparta.secureschedulerappserver.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentRequestDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();


    @Test
    @DisplayName("댓글에 공백을 넣어 validation 확인")
    void notBlankValidation(){
        // @NotBlank 테스트
        // given
        CommentRequestDto commentRequestDto = new CommentRequestDto("");

        // when
        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(commentRequestDto);

        // then
        assertFalse(violations.isEmpty()); // 위반 사항이 발견되었는지 확인
        assertEquals(1, violations.size()); // 발견된 위반 사항이 하나인지 확인
        ConstraintViolation<CommentRequestDto> violation = violations.iterator().next();
        assertEquals("공백일 수 없습니다", violation.getMessage()); // 올바른 메시지가 출력되었는지 확인
    }

    @Test
    @DisplayName("댓글에 정상문자를 넣어 validation 확인")
    void notBlankValidation2(){
        // @NotBlank 테스트
        // given
        String comment = "정상 댓글 테스트";
        CommentRequestDto commentRequestDto = new CommentRequestDto(comment);

        // when
        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(commentRequestDto);

        // then
        assertTrue(violations.isEmpty()); // 위반 사항이 발견되었는지 확인
        assertEquals(0, violations.size()); // 발견된 위반 사항이 하나인지 확인
        assertEquals("정상 댓글 테스트", commentRequestDto.getComment());
    }

}