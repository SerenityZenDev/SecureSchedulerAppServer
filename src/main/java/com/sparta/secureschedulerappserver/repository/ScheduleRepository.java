package com.sparta.secureschedulerappserver.repository;

import com.sparta.secureschedulerappserver.entity.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByUser_UserId(Long userId);

    List<Schedule> findByUser_UserIdAndHiddenFalse(Long userId);

    List<Schedule> findByUser_UserIdAndIsCompletedFalseAndHiddenFalse(Long userId);

    List<Schedule> findAllByTitleContainingAndHiddenFalse(String title);

}
