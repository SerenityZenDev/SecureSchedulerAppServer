package com.sparta.secureschedulerappserver.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleResponseDtoTest {

    @Test
    @DisplayName("ScheduleResponseDto 생성자 테스트 - 사용자 이름 포함")
    void testConstructorWithUsername() throws InterruptedException {
        // given
        User user = new User("tester", "12345678");
        Schedule schedule = new Schedule("테스트 제목", "테스트 내용", user);
        String username = "tester";

        // when
        ScheduleResponseDto dto = new ScheduleResponseDto(schedule, username);
        Thread.sleep(1000);

        // then
        assertEquals("테스트 제목", dto.getTitle(), "제목이 예상한 대로 설정되었는지 확인");
        assertEquals("테스트 내용", dto.getContent(), "내용이 예상한 대로 설정되었는지 확인");
        assertEquals("tester", dto.getUsername(), "사용자 이름이 예상한 대로 설정되었는지 확인");
        assertTrue(dto.getCreateAt().isBefore(LocalDateTime.now()), "생성일이 현재 시간 이전인지 확인");
        assertFalse(dto.isHidden(), "숨김 상태가 아닌지 확인");
    }

    @Test
    @DisplayName("ScheduleResponseDto 생성자 테스트 - 사용자 이름 미포함")
    void testConstructorWithoutUsername() throws InterruptedException {
        // given
        User user = new User("tester", "12345678");
        Schedule schedule = new Schedule("테스트 제목", "테스트 내용", user);

        // when
        ScheduleResponseDto dto = new ScheduleResponseDto(schedule);
        Thread.sleep(1000);

        // then
        assertEquals("테스트 제목", dto.getTitle(), "제목이 예상한 대로 설정되었는지 확인");
        assertEquals("테스트 내용", dto.getContent(), "내용이 예상한 대로 설정되었는지 확인");
        assertEquals("tester", dto.getUsername(), "사용자 이름이 예상한 대로 설정되었는지 확인");
        assertTrue(dto.getCreateAt().isBefore(LocalDateTime.now()), "생성일이 현재 시간 이전인지 확인");
        assertFalse(dto.isHidden(), "숨김 상태가 아닌지 확인");
    }
}