package com.sparta.secureschedulerappserver.exception;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException() {
        super("해당 유저 이름은 이미 존재합니다.");
    }
}
