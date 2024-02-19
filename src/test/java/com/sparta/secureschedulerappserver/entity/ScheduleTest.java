package com.sparta.secureschedulerappserver.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    @Test
    @DisplayName("스케쥴 생성 테스트")
    void createScheduleTest(){
        // given
        User user = new User("tester", "password");

        // when
        Schedule schedule = new Schedule("테스트 일정", "테스트 내용", user);

        // then
        assertNotNull(schedule);
        assertEquals("테스트 일정", schedule.getTitle());
        assertEquals("테스트 내용", schedule.getContent());
        assertEquals(user, schedule.getUser());
    }

    @Test
    @DisplayName("스케쥴 수정 테스트")
    void updateScheduleTest(){
        // given
        User user = new User("tester", "password");
        Schedule schedule = new Schedule("테스트 일정", "테스트 내용", user);
        String title = "수정 후 제목";
        String content = "수정 후 내용";

        // when
        schedule.update(title, content);

        // then
        assertEquals("수정 후 제목", schedule.getTitle());
        assertEquals("수정 후 내용", schedule.getContent());
    }

}