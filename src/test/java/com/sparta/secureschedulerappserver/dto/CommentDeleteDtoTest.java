package com.sparta.secureschedulerappserver.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CommentDeleteDtoTest {

    @Test
    @DisplayName("Getter가 잘 수행되고 있는가")
    void testGetter(){
        // Getter Test
        // given
        String message = "Comment successfully deleted";
        HttpStatus httpStatus = HttpStatus.OK;

        // when
        CommentDeleteDto commentDeleteDto = new CommentDeleteDto(message, httpStatus);

        // then
        assertNotNull(commentDeleteDto); // CommentDeleteDto 객체가 null이 아닌지 확인
        assertEquals(message, commentDeleteDto.getMessage()); // getMessage() 메서드가 예상된 값 반환하는지 확인
        assertEquals(httpStatus, commentDeleteDto.getHttpStatus()); // getHttpStatus() 메서드가 예상된 값 반환하는지 확인
    }
}