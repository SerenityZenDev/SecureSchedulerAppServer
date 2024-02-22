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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
//@ActiveProfiles("test")
class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ScheduleService scheduleService;

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
            ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto, userDetails);

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
            assertThrows(NotFoundUserException.class, () -> scheduleService.createSchedule(requestDto, userDetails));
            verify(scheduleRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("스케쥴 조회 관련 테스트")
    class ReadScheduleTest{
        @Test
        @DisplayName("스케쥴 1개 조회 테스트")
        void testReadOneTrue(){
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
            assertThrows(NotFoundScheduleException.class, () -> scheduleService.readSchedule(scheduleId));
        }
        @Test
        @DisplayName("스케쥴 여러개 조회 테스트") // 추가 수정 필요
        void testReadSchedules() {
            // given
            User user1 = new User("user1", "password1");
            User user2 = new User("user2", "password2");

            List<User> users = List.of(user1);
            when(userRepository.findAll()).thenReturn(users);

            List<Schedule> user1Schedules = List.of(
                new Schedule("User 1 Schedule 1", "Content 1", user1),
                new Schedule("User 1 Schedule 2", "Content 2", user1)
            );
            when(scheduleRepository.findByUser_UserIdAndHiddenFalse(user1.getUserId())).thenReturn(user1Schedules);

            List<Schedule> user2Schedules = List.of(
                new Schedule("User 2 Schedule 1", "Content 1", user2),
                new Schedule("User 2 Schedule 2", "Content 2", user2)
            );
            when(scheduleRepository.findByUser_UserIdAndHiddenFalse(user2.getUserId())).thenReturn(user2Schedules);

            // when
            ScheduleListResponseDto responseDto = scheduleService.readSchedules();

            // then
            assertNotNull(responseDto);
            assertEquals(2, responseDto.getScheduleByName().size()); // 두 사용자의 일정이 포함되어야 함

            // 사용자 1의 일정 확인
            assertTrue(responseDto.getScheduleByName().containsKey("user1"));
            assertEquals(2, responseDto.getScheduleByName().get("user1").size());

            // 사용자 2의 일정 확인
            assertTrue(responseDto.getScheduleByName().containsKey("user2"));
            assertEquals(2, responseDto.getScheduleByName().get("user2").size());
        }



        @Test
        @DisplayName("자신이 작성한 스케쥴 조회")
        void testreadMySchedule(){
            // given
            User user1 = new User("user1", "password1");
            UserDetailsImpl userDetails = new UserDetailsImpl(user1);

            when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

            List<Schedule> user1Schedules = List.of(
                new Schedule("User 1 Schedule 1", "Content 1", user1),
                new Schedule("User 1 Schedule 2", "Content 2", user1)
            );
            when(scheduleRepository.findByUser_UserId(user1.getUserId())).thenReturn(user1Schedules);

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
            User user = new User();
            user.setUsername("testUser");
            user.setUserId(1L);
            List<User> users = Collections.singletonList(user);
            given(userRepository.findAll()).willReturn(users);

            Schedule schedule1 = new Schedule();
            schedule1.complete();
            schedule1.setHidden(false);
            schedule1.setUser(user);
            List<Schedule> schedules = Collections.singletonList(schedule1);
            given(scheduleRepository.findByUser_UserIdAndIsCompletedFalseAndHiddenFalse(user.getUserId())).willReturn(schedules);

            // when
            ScheduleListResponseDto result = scheduleService.readUncompleteSchedules();

            // then
            assertEquals(1, result.getScheduleByName().size());
            assertTrue(result.getScheduleByName().containsKey(user.getUsername()));
            assertEquals(1, result.getScheduleByName().get(user.getUsername()).size());
        }

        @Test
        @DisplayName("미완료 일정 조회 테스트")
        void testReadUncompleteSchedules() {
            // given
            // 사용자와 스케줄 데이터 생성
            User user1 = new User("user1", "password1");
            User user2 = new User("user2", "password2");
            Schedule schedule1 = new Schedule("Schedule 1", "Content 1", user1);
            Schedule schedule2 = new Schedule("Schedule 2", "Content 2", user1);
            Schedule schedule3 = new Schedule("Schedule 3", "Content 3", user2);
            Schedule schedule4 = new Schedule("Schedule 4", "Content 4", user2);

            // schedule2를 완료된 일정으로 설정
            schedule2.setCompleted(true);

            List<User> users = List.of(user1, user2);
            List<Schedule> schedules = List.of(schedule1, schedule2, schedule3, schedule4);

            // userRepository findAll() 메서드에 대한 목 객체 생성 및 설정
            UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
            Mockito.when(userRepositoryMock.findAll()).thenReturn(users);

            // scheduleRepository findByUser_UserIdAndIsCompletedFalseAndHiddenFalse() 메서드에 대한 목 객체 생성 및 설정
            ScheduleRepository scheduleRepositoryMock = Mockito.mock(ScheduleRepository.class);
            given(scheduleRepositoryMock.findByUser_UserIdAndIsCompletedFalseAndHiddenFalse(Mockito.anyLong()))
                .willAnswer(invocation -> {
                    // 사용자의 ID를 가져와서 해당 사용자의 완료되지 않은 일정만 반환
                    Long userId = invocation.getArgument(0);
                    return schedules.stream()
                        .filter(schedule -> schedule.getUser().getUserId().equals(userId) && !schedule.isCompleted() && !schedule.isHidden())
                        .collect(Collectors.toList());
                });

            // when
            ScheduleListResponseDto responseDto = scheduleService.readUncompleteSchedules();

            // then
            assertNotNull(responseDto);
            Map<String, List<ScheduleResponseDto>> scheduleByName = responseDto.getScheduleByName();
            assertNotNull(scheduleByName);

            // user1의 일정은 schedule2를 제외하고 1개여야 함
            assertTrue(scheduleByName.containsKey("user1"));
            assertEquals(1, scheduleByName.get("user1").size());

            // user2의 일정은 모두 2개여야 함
            assertTrue(scheduleByName.containsKey("user2"));
            assertEquals(2, scheduleByName.get("user2").size());
        }


        @Test
        @DisplayName("제목에 특정 텍스트를 포함하는 일정을 찾는 테스트")
        void testFindSchedulesContainingText() {
            // given
            User fakeUser = new User("fakeUser", "12345678");
            String searchText = "meeting";
            Schedule schedule1 = new Schedule("Meeting with client","1", fakeUser);
            Schedule schedule2 = new Schedule("Team meeting", "2",fakeUser);
            Schedule schedule3 = new Schedule("Project review", "3", fakeUser);

            List<Schedule> mockSchedules = List.of(schedule1, schedule2, schedule3);

            when(scheduleRepository.findAllByTitleContainingAndHiddenFalse(searchText))
                .thenReturn(mockSchedules);

            // when
            List<ScheduleResponseDto> result = scheduleService.findSchedules(searchText);

            // then
            assertNotNull(result);
            assertEquals(2, result.size()); // "Meeting with client"과 "Team meeting" 두 개의 일정이 예상된다.
            assertEquals("Meeting with client", result.get(0).getTitle());
            assertEquals("Team meeting", result.get(1).getTitle());
        }

        @Test
        @DisplayName("제목에 특정 텍스트를 포함하지 않는 일정을 찾는 테스트")
        void testFindSchedulesNotContainingText() {
            // given
            User fakeUser = new User("fakeUser", "12345678");
            String searchText = "meeting";
            Schedule schedule1 = new Schedule("Project kickoff", "1", fakeUser);
            Schedule schedule2 = new Schedule("Weekly report", "2",fakeUser);

            List<Schedule> mockSchedules = List.of(schedule1, schedule2);

            when(scheduleRepository.findAllByTitleContainingAndHiddenFalse(searchText))
                .thenReturn(mockSchedules);

            // when
            List<ScheduleResponseDto> result = scheduleService.findSchedules(searchText);

            // then
            assertNotNull(result);
            assertEquals(0, result.size()); // 일치하는 일정이 없으므로 결과 리스트는 비어 있어야 한다.
        }
    }

    @Nested
    @DisplayName("일정 수정 테스트")
    class updateScheduleTest{
        @Test
        @DisplayName("일정 업데이트 테스트 - 성공")
        void testUpdateSchedule_Success() {
            // Given
            User user = new User("username", "password");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Schedule schedule = new Schedule("Title", "Content", user);
            ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto("New Title", "New Content");

            given(userRepository.findByUsername(userDetails.getUsername())).willReturn(Optional.of(user));
            given(scheduleRepository.findById(anyLong())).willReturn(Optional.of(schedule));

            // When
            ScheduleResponseDto responseDto = scheduleService.updateSchedule(1L, scheduleRequestDto, userDetails);

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
            ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto("New Title", "New Content");

            given(userRepository.findByUsername(userDetails.getUsername())).willReturn(Optional.of(user));
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
            ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto("New Title", "New Content");

            given(userRepository.findByUsername(userDetails.getUsername())).willReturn(Optional.of(new User()));
            given(scheduleRepository.findById(anyLong())).willReturn(Optional.empty());

            // When / Then
            assertThrows(NotFoundScheduleException.class,
                () -> scheduleService.updateSchedule(1L, scheduleRequestDto, userDetails));
        }

    }

}