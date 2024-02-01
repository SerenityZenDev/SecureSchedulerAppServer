package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.exception.DuplicateUsernameException;
import com.sparta.secureschedulerappserver.exception.NotFoundScheduleException;
import com.sparta.secureschedulerappserver.exception.NotFoundUserException;
import com.sparta.secureschedulerappserver.exception.UnauthorizedOperationException;
import com.sparta.secureschedulerappserver.exception.dto.ExceptionDto;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ScheduleErrorController {

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<ExceptionDto> NotFoundUser(NotFoundUserException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionDto(e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NotFoundScheduleException.class)
    public ResponseEntity<ExceptionDto> NotFoundSchedule(NotFoundScheduleException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionDto(e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ExceptionDto> UnauthorizedOperation(UnauthorizedOperationException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionDto(e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleMethodArgumentNotValid(
        MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest()
            .body(new ExceptionDto(errorMessage, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ExceptionDto> handleDuplicateUsernameException(
        DuplicateUsernameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ExceptionDto(e.getMessage(), HttpStatus.CONFLICT));
    }
}
