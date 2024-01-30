package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.dto.UserResponseDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        try {
            userService.join(userRequestDto);
            String successMessage = "회원가입이 성공적으로 완료되었습니다.";
            return new ResponseEntity<>(new UserResponseDto(successMessage, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "회원가입 중 오류가 발생했습니다. 다시 시도해주세요.";
            return new ResponseEntity<>(new UserResponseDto(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(
        @Valid @RequestBody UserRequestDto userRequestDto,
        HttpServletResponse res){
        try {
            userService.login(userRequestDto, res);
            String successMessage = "로그인이 성공적으로 완료되었습니다.";
            return new ResponseEntity<>(new UserResponseDto(successMessage, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "로그인 중 오류가 발생했습니다. 다시 시도해주세요.";
            return new ResponseEntity<>(new UserResponseDto(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
