package com.sparta.secureschedulerappserver.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ScheduleRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("유저 ID로 스케쥴 찾기")
    void findByUser_UserIdTest(){
        // given
        User user = new User("tester", "password1");
        userRepository.save(user);

        User fakeUser = new User("fakeTest", "password2");
        userRepository.save(fakeUser);

        Schedule schedule1 = new Schedule("title 1", "Test Schedule 1", user);
        Schedule schedule2 = new Schedule("title 2", "Test Schedule 2", user);
        Schedule schedule3 = new Schedule("title 3", "Test Schedule 3", fakeUser);

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);

        // when
        var resultScheduleList = scheduleRepository.findByUser_UserId(1L);

        // then
        assertEquals(resultScheduleList.get(0), schedule1);
        assertEquals(resultScheduleList.get(1), schedule2);
    }

    @Test
    @DisplayName("유저 ID로 숨기기 기능을 사용하지 않은 스케쥴 찾기")
    void findByUser_UserIdAndHiddenFalseTest(){
        // given
        User user = new User("tester", "password1");
        userRepository.save(user);

        User fakeUser = new User("fakeTest", "password2");
        userRepository.save(fakeUser);

        Schedule schedule1 = new Schedule("title 1", "Test Schedule 1", user);
        Schedule schedule2 = new Schedule("title 2", "Test Schedule 2", user);
        schedule2.optionHidden();
        Schedule schedule3 = new Schedule("title 3", "Test Schedule 3", fakeUser);

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);

        // when
        var resultScheduleList = scheduleRepository.findByUser_UserIdAndHiddenFalse(1L);

        // then
        assertEquals(resultScheduleList.get(0), schedule1);
    }

    @Test
    @DisplayName("유저 ID로 미완료 및 숨기기 기능을 사용하지 않은 스케쥴 찾기")
    void findByUser_UserIdAndIsCompletedFalseAndHiddenFalseTest(){
        // given
        User user = new User("tester", "password1");
        userRepository.save(user);

        User fakeUser = new User("fakeTest", "password2");
        userRepository.save(fakeUser);

        Schedule schedule1 = new Schedule("title 1", "Test Schedule 1", user);
        Schedule schedule2 = new Schedule("title 2", "Test Schedule 2", user);
        schedule2.optionHidden();
        Schedule schedule3 = new Schedule("title 3", "Test Schedule 3", user);
        schedule3.complete();
        Schedule schedule4 = new Schedule("title 4", "Test Schedule 4", fakeUser);

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);
        scheduleRepository.save(schedule4);

        // when
        var resultScheduleList = scheduleRepository.findByUser_UserIdAndIsCompletedFalseAndHiddenFalse(1L);

        // then
        assertEquals(resultScheduleList.get(0), schedule1);
    }

    @Test
    @DisplayName("제목에 입력된 내용이 포함되며 숨겨지지않은 스케쥴 조회")
    void findAllByTitleContainingAndHiddenFalseTest(){
        // given
        User user = new User("tester", "password1");
        userRepository.save(user);

        Schedule schedule1 = new Schedule("title 1", "Test Schedule 1", user);
        Schedule schedule2 = new Schedule("title 2", "Test Schedule 2", user);
        schedule2.optionHidden();
        Schedule schedule3 = new Schedule("title 3", "Test Schedule 3", user);

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);

        String title = "title";
        // when
        var resultScheduleList = scheduleRepository.findAllByTitleContainingAndHiddenFalse(title);

        // then
        assertEquals(resultScheduleList.get(0), schedule1);
        assertEquals(resultScheduleList.get(1), schedule3);
    }
}