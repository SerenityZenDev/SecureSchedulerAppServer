package com.sparta.secureschedulerappserver.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ScheduleRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @MockBean
    JPAQueryFactory queryFactory;

    // test객체 생성
    User user;
    User fakeUser;
    Schedule schedule1;
    Schedule schedule2;
    Schedule schedule3;
    Schedule schedule4;

    @BeforeEach
    void setUp() {
        user = new User("tester", "password1");
        userRepository.save(user);

        fakeUser = new User("fakeTest", "password2");
        userRepository.save(fakeUser);

        schedule1 = new Schedule("title 1", "Test Schedule 1", user);
        schedule2 = new Schedule("title 2", "Test Schedule 2", user);
        schedule2.optionHidden();
        schedule3 = new Schedule("title 3", "Test Schedule 3", user);
        schedule3.complete();
        schedule4 = new Schedule("title 4", "Test Schedule 4", fakeUser);

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);
        scheduleRepository.save(schedule4);
    }

    @Test
    @DisplayName("유저 ID로 스케쥴 찾기")
    void findByUser_UserIdTest() {
        // given

        // when
        var resultScheduleList = scheduleRepository.findByUser_UserId(schedule1.getUser().getUserId());

        // then
        assertEquals(resultScheduleList.get(0), schedule1);
        assertEquals(resultScheduleList.get(1), schedule2);
    }

    @Test
    @DisplayName("유저 ID로 숨기기 기능을 사용하지 않은 스케쥴 찾기")
    void findByUser_UserIdAndHiddenFalseTest() {
        // given


        // when
        var allSchedule = scheduleRepository.findAll();
        var resultScheduleList = scheduleRepository.findByUser_UserIdAndHiddenFalse(schedule1.getUser().getUserId());

        // then
        assertEquals(resultScheduleList.get(0), schedule1);
    }

    @Test
    @DisplayName("유저 ID로 미완료 및 숨기기 기능을 사용하지 않은 스케쥴 찾기")
    void findByUser_UserIdAndIsCompletedFalseAndHiddenFalseTest() {
        // given


        // when
        var resultScheduleList = scheduleRepository.findByUser_UserIdAndIsCompletedFalseAndHiddenFalse(
            schedule1.getUser().getUserId());

        // then
        assertEquals(resultScheduleList.get(0).getScheduleId(), schedule1.getScheduleId());
    }

    @Test
    @DisplayName("제목에 입력된 내용이 포함되며 숨겨지지않은 스케쥴 조회")
    void findAllByTitleContainingAndHiddenFalseTest() {
        // given


        String title = "title";
        // when
        var resultScheduleList = scheduleRepository.findAllByTitleContainingAndHiddenFalse(title);

        // then
        assertEquals(resultScheduleList.get(0).getScheduleId(), schedule1.getScheduleId());
        assertEquals(resultScheduleList.get(1).getScheduleId(), schedule3.getScheduleId());
    }
}