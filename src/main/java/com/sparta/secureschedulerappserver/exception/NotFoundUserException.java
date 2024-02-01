package com.sparta.secureschedulerappserver.exception;

public class NotFoundUserException extends IllegalArgumentException {

    public NotFoundUserException() {
        super("회원을 찾을 수 없습니다.");
    }
}
