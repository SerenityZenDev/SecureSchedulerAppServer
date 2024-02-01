package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.dto.UserResponseDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.dto.ExceptionDto;
import com.sparta.secureschedulerappserver.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join (@Valid @RequestBody UserRequestDto userRequestDto){

        userService.join(userRequestDto);
        String successMessage = "회원가입이 성공적으로 완료되었습니다.";

        return ResponseEntity.ok().body(new UserResponseDto(successMessage,HttpStatus.OK ));
    }
}
