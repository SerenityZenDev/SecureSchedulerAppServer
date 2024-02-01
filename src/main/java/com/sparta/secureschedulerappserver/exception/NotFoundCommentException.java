package com.sparta.secureschedulerappserver.exception;

public class NotFoundCommentException extends IllegalArgumentException {

    public NotFoundCommentException() {
        super("해당 댓글을 찾을 수 없습니다.");
    }

}
