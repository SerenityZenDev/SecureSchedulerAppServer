package com.sparta.secureschedulerappserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.secureschedulerappserver.dto.PageDto;
import com.sparta.secureschedulerappserver.dto.ScheduleListResponseDto;
import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.NotFoundScheduleException;
import com.sparta.secureschedulerappserver.exception.NotFoundUserException;
import com.sparta.secureschedulerappserver.exception.UnauthorizedOperationException;
import com.sparta.secureschedulerappserver.repository.ScheduleRepository;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
@ActiveProfiles("test")
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Nested
    @DisplayName("일정 생성 관련 테스트")
    class CreateScheduleTests {

        @Test
        @DisplayName("일정 생성 테스트")
        void testCreateSchedule() {
            // given
            ScheduleRequestDto requestDto = new ScheduleRequestDto("새로운 일정", "일정 내용");

            User user = new User("testUser", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

            // when
            ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto,
                userDetails);

            // then
            assertNotNull(responseDto);
            assertEquals("새로운 일정", responseDto.getTitle());
            assertEquals("일정 내용", responseDto.getContent());
            assertEquals("testUser", responseDto.getUsername());
            // scheduleRepository.save() 메서드가 한 번 호출되고, 어떤 Schedule 객체를 매개변수로 받아 호출되었는지를 검증
            verify(scheduleRepository, times(1)).save(any(Schedule.class));
        }

        @Test
        @DisplayName("사용자가 없을 때 예외 발생 확인")
        void testCreateSchedule_UserNotFound() {
            // given
            User user = new User("testUser", "password");
            ScheduleRequestDto requestDto = new ScheduleRequestDto("새로운 일정", "일정 내용");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

            // when
            // then
            assertThrows(NotFoundUserException.class,
                () -> scheduleService.createSchedule(requestDto, userDetails));
            verify(scheduleRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("스케쥴 조회 관련 테스트")
    class ReadScheduleTest {

        @Test
        @DisplayName("스케쥴 1개 조회 테스트")
        void testReadOneTrue() {
            // given
            Long scheduleId = 1L;
            Schedule schedule = new Schedule("Test Title", "Test Content", new User());
            when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

            // when
            ScheduleResponseDto responseDto = scheduleService.readSchedule(scheduleId);

            // then
            assertNotNull(responseDto);
            assertEquals(schedule.getTitle(), responseDto.getTitle());
            assertEquals(schedule.getContent(), responseDto.getContent());
        }

        @Test
        @DisplayName("스케쥴 1개 조회 시 없을 경우 테스트")
        void testReadOneFalse() {
            // given
            Long scheduleId = 1L;
            when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

            // when & then
            assertThrows(NotFoundScheduleException.class,
                () -> scheduleService.readSchedule(scheduleId));
        }

        @Test
        @DisplayName("스케쥴 여러 개 조회 테스트")
        void testReadSchedules() {
            // given
            User user1 = new User("user1", "password1");
            user1.setUserId(1L); // UserId 설정
            User user2 = new User("user2", "password2");
            user2.setUserId(2L); // UserId 설정

            List<User> users = List.of(user1, user2);
            when(userRepository.findAll()).thenReturn(users);

            List<Schedule> user1Schedules = List.of(
                new Schedule("User 1 Schedule 1", "Content 1", user1),
                new Schedule("User 1 Schedule 2", "Content 2", user1)
            );
            when(scheduleRepository.findByUser_UserIdAndHiddenFalse(user1.getUserId())).thenReturn(
                user1Schedules);

            List<Schedule> user2Schedules = List.of(
                new Schedule("User 2 Schedule 1", "Content 1", user2),
                new Schedule("User 2 Schedule 2", "Content 2", user2)
            );
            when(scheduleRepository.findByUser_UserIdAndHiddenFalse(user2.getUserId())).thenReturn(
                user2Schedules);

            // when
            ScheduleListResponseDto responseDto = scheduleService.readSchedules();

            // then
            assertNotNull(responseDto);
            assertEquals(2, responseDto.getScheduleByName().size()); // 두 사용자의 일정이 포함되어야 함

            // 사용자 1의 일정 확인
            assertTrue(responseDto.getScheduleByName().containsKey(user1.getUsername()));
            assertEquals(2, responseDto.getScheduleByName().get(user1.getUsername()).size());

            // 사용자 2의 일정 확인
            assertTrue(responseDto.getScheduleByName().containsKey(user2.getUsername()));
            assertEquals(2, responseDto.getScheduleByName().get(user2.getUsername()).size());
        }


        @Test
        @DisplayName("자신이 작성한 스케쥴 조회")
        void testreadMySchedule() {
            // given
            User user1 = new User("user1", "password1");
            UserDetailsImpl userDetails = new UserDetailsImpl(user1);

            when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

            List<Schedule> user1Schedules = List.of(
                new Schedule("User 1 Schedule 1", "Content 1", user1),
                new Schedule("User 1 Schedule 2", "Content 2", user1)
            );
            when(scheduleRepository.findByUser_UserId(user1.getUserId())).thenReturn(
                user1Schedules);

            // when
            ScheduleListResponseDto responseDto = scheduleService.readMySchedules(userDetails);

            // then
            assertNotNull(responseDto);
            assertEquals(1, responseDto.getScheduleByName().size()); // 사용자가 1명이어야 함
            assertTrue(responseDto.getScheduleByName().containsKey("user1"));
            assertEquals(2, responseDto.getScheduleByName().get("user1").size());
        }

        @Test
        public void readUncompleteSchedulesTest() {
            // given
            User user = new User(1L, "testUser");
            List<User> users = Collections.singletonList(user);
            given(userRepository.findAll()).willReturn(users);

            Schedule schedule1 = new Schedule("test", "content", user);
            schedule1.complete();
            schedule1.optionHidden();
            List<Schedule> schedules = Collections.singletonList(schedule1);
            given(scheduleRepository.findByUser_UserIdAndIsCompletedFalseAndHiddenFalse(
                user.getUserId())).willReturn(schedules);

            // when
            ScheduleListResponseDto result = scheduleService.readUncompleteSchedules();

            // then
            assertEquals(1, result.getScheduleByName().size());
            assertTrue(result.getScheduleByName().containsKey(user.getUsername()));
            assertEquals(1, result.getScheduleByName().get(user.getUsername()).size());
        }


        @Test
        @DisplayName("제목에 특정 텍스트를 포함하는 일정을 찾는 테스트")
        void testFindSchedules() {
            // given
            String searchText = "meeting";
            User user = new User("user1", "password1");
            List<Schedule> schedules = List.of(
                new Schedule("Weekly meeting", "Content 1", user),
                new Schedule("Project meeting", "Content 2", user)
            );

            when(scheduleRepository.findAllByTitleContainingAndHiddenFalse(searchText)).thenReturn(
                schedules);

            // when
            List<ScheduleResponseDto> result = scheduleService.findSchedules(searchText);

            // then
            assertNotNull(result); // 결과가 null이 아닌지 확인
            assertEquals(2, result.size()); // 조회 결과의 크기가 기대하는 값인 2인지 확인

            // 반환된 ScheduleResponseDto 리스트의 내용 검증 (선택적)
            assertEquals("Weekly meeting", result.get(0).getTitle());
            assertEquals("Project meeting", result.get(1).getTitle());
        }

        @Test
        @DisplayName("제목에 특정 텍스트를 포함하지 않는 일정을 찾는 테스트")
        void testFindSchedulesNotContainingText() {
            // given
            User fakeUser = new User("fakeUser", "12345678");
            String searchText = "meeting";
            Schedule schedule1 = new Schedule("Project kickoff", "1", fakeUser);
            Schedule schedule2 = new Schedule("Weekly report", "2", fakeUser);

            List<Schedule> mockSchedules = List.of(schedule1, schedule2);
            List<Schedule> readSchduleResult = new ArrayList<>();

            when(scheduleRepository.findAllByTitleContainingAndHiddenFalse(searchText))
                .thenReturn(readSchduleResult);

            // when
            List<ScheduleResponseDto> result = scheduleService.findSchedules(searchText);

            // then
            assertNotNull(result);
            assertEquals(0, result.size()); // 일치하는 일정이 없으므로 결과 리스트는 비어 있어야 한다.
        }

        @Test
        void showSchedulesTest() {
            // given
            User fakeUser = new User("fakeUser", "12345678");
            String keyword = "meeting";
            PageDto pageDto = PageDto.builder().currentPage(1).size(1)
                .build(); // 예를 들어 페이지 0, 사이즈 10
            List<Schedule> foundSchedules = Arrays.asList(
                new Schedule("Meeting with client", "Discuss project", fakeUser),
                new Schedule("Team meeting", "Weekly update", fakeUser)
            );

            // Mocking
            when(scheduleRepository.searchByTitle(keyword, pageDto.toPageable()))
                .thenReturn(new PageImpl<>(foundSchedules));

            // when
            List<ScheduleResponseDto> result = scheduleService.showSchedules(keyword, pageDto);

            // then
            assertEquals(2, result.size()); // 반환된 DTO 리스트의 크기 검증
            assertEquals("Meeting with client", result.get(0).getTitle()); // 첫 번째 항목의 제목 검증
            assertEquals("Team meeting", result.get(1).getTitle()); // 두 번째 항목의 제목 검증
        }

        @Test
        void hideScheduleTest() {
            // given
            Long scheduleId = 1L;
            User user = new User("user1", "password1");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Schedule schedule = new Schedule("Team meeting", "Weekly update", user);

            when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(
                java.util.Optional.of(user));
            when(scheduleRepository.findById(scheduleId)).thenReturn(
                java.util.Optional.of(schedule));

            // when
            ScheduleResponseDto responseDto = scheduleService.hideSchedule(scheduleId, userDetails);

            // then
            verify(scheduleRepository).findById(anyLong());
            verify(userRepository).findByUsername(userDetails.getUsername());
            assertEquals(user.getUsername(), responseDto.getUsername()); // 사용자 이름 검증
            assertEquals(schedule.getTitle(), responseDto.getTitle()); // 숨겨진 일정의 제목 검증
            assertTrue(responseDto.isHidden()); // 일정이 숨겨졌는지 검증
        }

        @Test
        void completeScheduleTest() {
            // given
            Long scheduleId = 1L;
            User user = new User("user1", "password1");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Schedule schedule = new Schedule("Team meeting", "Weekly update", user);

            when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(
                java.util.Optional.of(user));
            when(scheduleRepository.findById(scheduleId)).thenReturn(
                java.util.Optional.of(schedule));

            // when
            ScheduleResponseDto responseDto = scheduleService.completeSchedule(scheduleId,
                userDetails);

            // then
            verify(scheduleRepository).findById(anyLong());
            verify(userRepository).findByUsername(userDetails.getUsername());
            assertEquals(user.getUsername(), responseDto.getUsername()); // 사용자 이름 검증
            assertEquals(schedule.getTitle(), responseDto.getTitle()); // 완료된 일정의 제목 검증
            assertTrue(responseDto.isCompleted()); // 일정이 완료되었는지 검증
        }

        @Test
        void getSchedulesForUserTest() {
            // given
            User user = new User("user1", "password1");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            userDetails.getUser().setUserId(1L); // 사용자 ID 설정
            PageDto pageDto = PageDto.builder().currentPage(1).size(1)
                .build(); // 예: 페이지 0에서 시작, 페이지 당 10개 항목

            List<Schedule> foundSchedules = Arrays.asList(
                new Schedule("Meeting with client", "Discuss project", userDetails.getUser()),
                new Schedule("Team meeting", "Weekly update", userDetails.getUser())
            );
            Page<Schedule> schedulesPage = new PageImpl<>(foundSchedules);

            when(userRepository.findById(userDetails.getUser().getUserId())).thenReturn(
                java.util.Optional.of(userDetails.getUser()));
            when(scheduleRepository.getSchedulesForUser(any(Long.class),
                any(Pageable.class))).thenReturn(schedulesPage);

            // when
            List<ScheduleResponseDto> result = scheduleService.getSchedulesForUser(userDetails,
                pageDto);

            // then
            assertEquals(2, result.size()); // 반환된 DTO 리스트의 크기 검증
            assertEquals("Meeting with client", result.get(0).getTitle()); // 첫 번째 항목의 제목 검증
            assertEquals("Team meeting", result.get(1).getTitle()); // 두 번째 항목의 제목 검증
        }
    }

    @Nested
    @DisplayName("일정 수정 테스트")
    class updateScheduleTest {

        @Test
        @DisplayName("일정 업데이트 테스트 - 성공")
        void testUpdateSchedule_Success() {
            // Given
            User user = new User("username", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Schedule schedule = new Schedule("Title", "Content", user);
            ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto("New Title",
                "New Content");

            given(userRepository.findByUsername(userDetails.getUsername())).willReturn(
                Optional.of(user));
            given(scheduleRepository.findById(anyLong())).willReturn(Optional.of(schedule));

            // When
            ScheduleResponseDto responseDto = scheduleService.updateSchedule(1L, scheduleRequestDto,
                userDetails);

            // Then
            assertNotNull(responseDto);
            assertEquals(scheduleRequestDto.getTitle(), responseDto.getTitle());
            assertEquals(scheduleRequestDto.getContent(), responseDto.getContent());
        }

        @Test
        @DisplayName("일정 업데이트 테스트 - 사용자 불일치로 실패")
        void testUpdateSchedule_Failure_UserMismatch() {
            // Given
            User user = new User("username", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            User fakeUser = new User("fakeUser", "password"); // fakeUser로 변경

            Schedule schedule = new Schedule("Title", "Content", fakeUser);
            ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto("New Title",
                "New Content");

            given(userRepository.findByUsername(userDetails.getUsername())).willReturn(
                Optional.of(user));
            given(scheduleRepository.findById(anyLong())).willReturn(Optional.of(schedule));

            // When // Then
            assertThrows(UnauthorizedOperationException.class,
                () -> scheduleService.updateSchedule(1L, scheduleRequestDto, userDetails));
        }

        @Test
        @DisplayName("일정 업데이트 테스트 - 일정 미존재로 실패")
        void testUpdateSchedule_Failure_ScheduleNotFound() {
            // Given
            User user = new User("username", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto("New Title",
                "New Content");

            given(userRepository.findByUsername(userDetails.getUsername())).willReturn(
                Optional.of(new User()));
            given(scheduleRepository.findById(anyLong())).willReturn(Optional.empty());

            // When / Then
            assertThrows(NotFoundScheduleException.class,
                () -> scheduleService.updateSchedule(1L, scheduleRequestDto, userDetails));
        }

    }

}