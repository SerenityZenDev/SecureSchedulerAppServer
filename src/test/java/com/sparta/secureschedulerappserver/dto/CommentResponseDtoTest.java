package com.sparta.secureschedulerappserver.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.secureschedulerappserver.entity.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentResponseDtoTest {

    @Test
    @DisplayName("정상적인 댓글을 입력하였을 경우")
    void testCorrect(){
        // given
        Comment comment = new Comment();
        comment.setComment("테스트 코드 댓글");

        // when
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        // then
        assertEquals("테스트 코드 댓글", commentResponseDto.getComment(), "댓글이 정상적으로 설정될 경우 CommentResponseDto의 comment 필드도 동일한 값이어야 합니다.");
    }

    @Test
    @DisplayName("댓글이 null일 경우")
    void testNull(){
        // given
        Comment comment = new Comment();
        // comment.setComment("테스트 코드 댓글");

        // when
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        // then
        assertNull(commentResponseDto.getComment(),"댓글이 Null일 경우 CommentResponseDto의 comment 필드도 Null이어야 합니다.");
    }

    @Test
    @DisplayName("댓글이 빈 댓글일 경우")
    void testBlank(){
        // given
        Comment comment = new Comment();
        comment.setComment("");

        // when
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        // then
        assertEquals("", commentResponseDto.getComment(), "빈 댓글인 경우 CommentResponseDto의 comment 필드도 빈 문자열이어야 합니다.");
    }

    @Test
    @DisplayName("공백 포함 댓글인 경우")
    void testHasBlank(){
        // given
        Comment comment = new Comment();
        comment.setComment("   ");

        // when
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        // then
        assertEquals("   ", commentResponseDto.getComment(), "빈 문자열과 공백만 포함한 경우도 CommentResponseDto의 comment 필드에 올바르게 설정되어야 합니다.");
    }

}