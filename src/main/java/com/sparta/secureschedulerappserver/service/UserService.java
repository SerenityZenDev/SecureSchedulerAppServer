package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User join(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) throw new IllegalArgumentException("중복된 사용자가 존재합니다.");

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);

        return user;
    }
}
