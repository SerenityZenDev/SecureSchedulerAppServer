package com.sparta.secureschedulerappserver.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CommentDeleteDto {

    private final String message;
    private final HttpStatus httpStatus;
}
