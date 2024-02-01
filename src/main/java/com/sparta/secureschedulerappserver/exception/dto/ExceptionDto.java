package com.sparta.secureschedulerappserver.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ExceptionDto {
    private final String message;
    private final HttpStatus httpStatus;

}
