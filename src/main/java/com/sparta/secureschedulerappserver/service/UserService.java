package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.DuplicateUsernameException;
import com.sparta.secureschedulerappserver.jwt.JwtUtil;
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

    public void join(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DuplicateUsernameException();
        }

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);
    }

//    public void login(UserRequestDto userRequestDto, HttpServletResponse res) {
//        String username = userRequestDto.getUsername();
//        String password = userRequestDto.getPassword();
//
//        // 사용자 확인
//        User user = userRepository.findByUsername(username).orElseThrow(
//            () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
//        );
//
//        // 비밀번호 확인
//        if (!passwordEncoder.matches(password, user.getPassword())){
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
//        String token = jwtUtil.createToken(user.getUsername());
//        jwtUtil.addJwtToCookie(token, res);
//    }
}
