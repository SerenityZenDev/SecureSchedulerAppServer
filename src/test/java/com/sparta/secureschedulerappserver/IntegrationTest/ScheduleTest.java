package com.sparta.secureschedulerappserver.IntegrationTest;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.ScheduleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 서버의 PORT 를 랜덤으로 설정합니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScheduleTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleServiceImpl scheduleService;

    User user;


    ScheduleResponseDto createSchedule = null;

    @Test
    @Order(1)
    @DisplayName("할일 생성")
    void test1(){
        // given
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto("title1", "content1");

        user = userRepository.findById(1L).orElse(null);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // when
        ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(scheduleRequestDto, userDetails);

        // then
        assertNotNull(scheduleResponseDto.getTitle());
        assertEquals(scheduleRequestDto.getTitle(),scheduleResponseDto.getTitle());

        createSchedule = scheduleResponseDto;

    }

    @Test
    @Order(2)
    @DisplayName("할일 수정")
    void test2(){
        // given
        String title = this.createSchedule.getTitle();
        String content = "수정된 내용";

        user = userRepository.findById(1L).orElse(null);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto(title, content);

        // when
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(1L, scheduleRequestDto, userDetails);

        // then
        assertNotNull(responseDto);
        assertEquals(title, responseDto.getTitle());

    }

    @Test
    @Order(3)
    @DisplayName("할일 조회")
    void test3(){
        // when
        ScheduleResponseDto responseDto = scheduleService.readSchedule(1L);

        // then
        assertNotNull(responseDto);
        assertEquals(responseDto.getContent(), "수정된 내용");

    }


}
