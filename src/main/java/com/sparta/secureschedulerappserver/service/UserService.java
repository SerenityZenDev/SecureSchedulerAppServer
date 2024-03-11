package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.exception.PasswordMismatchException;

public interface UserService {

    /**
     * 회원 생성
     *
     * @param userRequestDto 회원가입에 사용되는 회원 정보
     */
    void join(UserRequestDto userRequestDto);

    void login(UserRequestDto userRequestDto) throws PasswordMismatchException;
}
