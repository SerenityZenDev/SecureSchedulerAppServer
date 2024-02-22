package com.sparta.secureschedulerappserver.dto;

import com.sparta.secureschedulerappserver.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    Long id;
    String comment;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getCommentId();
        this.comment = comment.getComment();
    }
}
