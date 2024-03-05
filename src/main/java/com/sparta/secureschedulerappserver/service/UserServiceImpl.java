package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.DuplicateUsernameException;
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

    private void validateDuplicateUsername(String username) {
        userRepository.findByUsername(username).orElseThrow(
            DuplicateUsernameException::new
        );
    }
}

