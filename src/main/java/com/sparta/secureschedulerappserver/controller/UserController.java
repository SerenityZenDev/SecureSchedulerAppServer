package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.dto.UserResponseDto;
import com.sparta.secureschedulerappserver.service.UserService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;
    @PostMapping("/join")
    public UserResponseDto join(@RequestBody UserRequestDto userRequestDto){
        return null;
    }

    @PostMapping("/login")
    public UserResponseDto login(@RequestBody UserRequestDto userRequestDto){
        return null;
    }
}
