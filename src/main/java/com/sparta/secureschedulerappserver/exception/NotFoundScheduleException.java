package com.sparta.secureschedulerappserver.exception;

public class NotFoundScheduleException extends IllegalArgumentException{

    public NotFoundScheduleException() {
        super("해당 스케쥴을 찾을 수 없습니다.");
    }

}
