package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.dto.UserResponseDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.PasswordMismatchException;
import com.sparta.secureschedulerappserver.jwt.JwtTokenError;
import com.sparta.secureschedulerappserver.jwt.JwtUtil;
import com.sparta.secureschedulerappserver.redis.RefreshTokenRedisRepository;
import com.sparta.secureschedulerappserver.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
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

    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join(@Valid @RequestBody UserRequestDto userRequestDto) {

        userService.join(userRequestDto);
        String successMessage = "회원가입이 성공적으로 완료되었습니다.";

        return ResponseEntity.ok().body(new UserResponseDto(successMessage, HttpStatus.OK));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "swagger 요청을 위한 가짜 엔드포인트")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<UserResponseDto> login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response)
        throws IOException {
        User user = userService.login(userRequestDto);
        // Access Token 생성
        String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getUsername());
        jwtUtil.addAccessTokenToCookie(accessToken, response);
        // Refresh Token 생성
        String refreshToken = jwtUtil.createRefreshToken();
        refreshTokenRedisRepository.save(accessToken, refreshToken);
        System.out.println(refreshTokenRedisRepository.findByKey(accessToken));

        String successMessage = "로그인이 성공적으로 완료되었습니다.";
        return ResponseEntity.ok().body(new UserResponseDto(successMessage, HttpStatus.OK));

    }
}
