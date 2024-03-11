package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.DuplicateUsernameException;
import com.sparta.secureschedulerappserver.exception.NotFoundUserException;
import com.sparta.secureschedulerappserver.exception.PasswordMismatchException;
import com.sparta.secureschedulerappserver.jwt.JwtTokenError;
import com.sparta.secureschedulerappserver.jwt.JwtUtil;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void join(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        // 회원 중복 확인
        validateDuplicateUsername(username);

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);
    }

    @Override
    public void login(UserRequestDto userRequestDto) throws PasswordMismatchException {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
            NotFoundUserException::new
        );

        validatePassword(user, password);

    }

    private void validateDuplicateUsername(String username) {
        userRepository.findByUsername(username).ifPresent(
            user -> {throw new DuplicateUsernameException();
            });
    }

    private void validatePassword(User user, String password) throws PasswordMismatchException {
        if(!passwordEncoder.matches(password, user.getPassword() )){
            throw new PasswordMismatchException();
        }
    }
}

