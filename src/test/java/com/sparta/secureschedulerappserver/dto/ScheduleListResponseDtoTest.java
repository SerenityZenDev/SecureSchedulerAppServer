package com.sparta.secureschedulerappserver.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleListResponseDtoTest {
    @Test
    @DisplayName("이중 json dto에서 데이터를 잘 가져올 수 있는지")
    void testScheduleListResponseDto(){
        // given
        Map<String, List<ScheduleResponseDto>> scheduleByName = new HashMap<>();
        List<ScheduleResponseDto> listResponseDtos = new ArrayList<>();

        User user1 = new User("tester", "12345678");
        Schedule schedule1 = new Schedule("테스트 중입니다 1.", "테스트 내용입니다 1.", user1);
        Schedule schedule2 = new Schedule("테스트 중입니다 2.", "테스트 내용입니다 2.", user1);

        User user2 = new User("tester2", "123456782");
        Schedule schedule3 = new Schedule("테스트 중입니다 3.", "테스트 내용입니다 3.", user2);
        Schedule schedule4 = new Schedule("테스트 중입니다 4.", "테스트 내용입니다 4.", user2);

        listResponseDtos.add(new ScheduleResponseDto(schedule1));
        listResponseDtos.add(new ScheduleResponseDto(schedule2));
        listResponseDtos.add(new ScheduleResponseDto(schedule3));
        listResponseDtos.add(new ScheduleResponseDto(schedule4));

        scheduleByName.put("test", listResponseDtos);


        // when
        ScheduleListResponseDto scheduleListResponseDto = new ScheduleListResponseDto(scheduleByName);

        // then
        assertNotNull(scheduleListResponseDto);
        assertTrue(scheduleListResponseDto.getScheduleByName().containsKey("test"));

        // 예상된 유저명 확인
        assertEquals("tester", scheduleListResponseDto.getScheduleByName().get("test").get(0).getUsername());
        assertEquals("tester", scheduleListResponseDto.getScheduleByName().get("test").get(1).getUsername());
        assertEquals("tester2", scheduleListResponseDto.getScheduleByName().get("test").get(2).getUsername());
        assertEquals("tester2", scheduleListResponseDto.getScheduleByName().get("test").get(3).getUsername());

        // 예상된 스케줄 내용 확인
        assertEquals("테스트 중입니다 1.", scheduleListResponseDto.getScheduleByName().get("test").get(0).getTitle());
        assertEquals("테스트 내용입니다 1.", scheduleListResponseDto.getScheduleByName().get("test").get(0).getContent());

        assertEquals("테스트 중입니다 2.", scheduleListResponseDto.getScheduleByName().get("test").get(1).getTitle());
        assertEquals("테스트 내용입니다 2.", scheduleListResponseDto.getScheduleByName().get("test").get(1).getContent());

        assertEquals("테스트 중입니다 3.", scheduleListResponseDto.getScheduleByName().get("test").get(2).getTitle());
        assertEquals("테스트 내용입니다 3.", scheduleListResponseDto.getScheduleByName().get("test").get(2).getContent());

        assertEquals("테스트 중입니다 4.", scheduleListResponseDto.getScheduleByName().get("test").get(3).getTitle());
        assertEquals("테스트 내용입니다 4.", scheduleListResponseDto.getScheduleByName().get("test").get(3).getContent());
    }

}