package com.sparta.secureschedulerappserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.DuplicateUsernameException;
import com.sparta.secureschedulerappserver.exception.NotFoundUserException;
import com.sparta.secureschedulerappserver.exception.PasswordMismatchException;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("로그인 테스트")
    class JoinTest {

        @Test
        void loginSuccessTest() throws PasswordMismatchException {
            // given
            String username = "testUser";
            String password = "testPassword";
            UserRequestDto userRequestDto = new UserRequestDto(username, password);

            // 사용자가 저장된 비밀번호 (여기서는 예시로 인코딩된 비밀번호를 직접 제공)
            String encodedPassword = "encodedTestPassword";
            User expectedUser = new User(username, encodedPassword);

            when(userRepository.findByUsername(username)).thenReturn(
                java.util.Optional.of(expectedUser));
            // 실제 비밀번호와 인코딩된 비밀번호가 일치한다고 가정
            when(passwordEncoder.matches(password, expectedUser.getPassword())).thenReturn(true);

            // when
            User resultUser = userService.login(userRequestDto);

            // then
            assertEquals(expectedUser.getUsername(), resultUser.getUsername());
        }

        @Test
        void loginFailUserNotFoundTest() {
            // given
            String username = "nonExistingUser";
            String password = "testPassword";
            UserRequestDto userRequestDto = new UserRequestDto(username, password);

            when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());

            // then
            assertThrows(NotFoundUserException.class, () -> {
                // when
                userService.login(userRequestDto);
            });
        }

        @Test
        void loginFailPasswordMismatchTest() {
            // given
            String username = "testUser";
            String wrongPassword = "wrongPassword";
            UserRequestDto userRequestDto = new UserRequestDto(username, wrongPassword);
            User user = new User(username, "correctPassword");

            when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

            // then
            assertThrows(PasswordMismatchException.class, () -> {
                // when
                userService.login(userRequestDto);
            });
        }
    }
}