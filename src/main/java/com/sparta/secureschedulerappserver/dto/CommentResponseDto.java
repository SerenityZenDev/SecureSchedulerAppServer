package com.sparta.secureschedulerappserver.dto;

import com.sparta.secureschedulerappserver.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    String comment;

    public CommentResponseDto(Comment comment) {
        this.comment = comment.getComment();
    }
}
