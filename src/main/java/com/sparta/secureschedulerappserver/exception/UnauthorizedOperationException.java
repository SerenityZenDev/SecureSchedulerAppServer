package com.sparta.secureschedulerappserver.exception;

public class UnauthorizedOperationException extends RuntimeException {

    public UnauthorizedOperationException() {
        super("작성자만 삭제/수정할 수 있습니다.");
    }
}
