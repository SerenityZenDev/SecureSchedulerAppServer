package com.sparta.secureschedulerappserver.repository;

import com.sparta.secureschedulerappserver.entity.Schedule;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleRepositoryQuery {
    Page<Schedule> searchByTitle(String title, Pageable pageable);

    Page<Schedule> getSchedulesForUser(Long userId, Pageable pageable);
}
