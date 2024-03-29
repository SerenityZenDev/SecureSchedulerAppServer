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

class ScheduleRequestDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();


    @Test
    @DisplayName("제목, 내용에 공백을 넣어 @NotBlank검증")
    void notBlankValidation(){
        // given
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto("","");

        // when
        Set<ConstraintViolation<ScheduleRequestDto>> violations = validator.validate(scheduleRequestDto);

        // then
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        for (ConstraintViolation<ScheduleRequestDto> violation : violations) {
            System.out.println(violation.getMessage());
            assertTrue(violation.getMessage().contains("공백일 수 없습니다"));
        }
    }

    @Test
    @DisplayName("정상 값을 넣고 Getter 확인")
    void dtoGetter(){
        // given
        String title = "제목 테스트";
        String content = "내용 테스트";

        // when
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto(title, content);

        // then
        assertEquals("제목 테스트", scheduleRequestDto.getTitle());
        assertEquals("내용 테스트", scheduleRequestDto.getContent());

    }

}