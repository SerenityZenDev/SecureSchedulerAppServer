package com.sparta.secureschedulerappserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.DuplicateUsernameException;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
//@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("중복 회원 확인")
    void testJoin() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("testUser", "testPassword");
        User existingUser = new User("testUser", "encodedPassword");

        // when
        // 회원 중복 확인을 위한 Mock 설정
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(existingUser));

         // then
        assertThrows(DuplicateUsernameException.class, () -> userService.join(userRequestDto));
    }
}