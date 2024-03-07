package com.sparta.secureschedulerappserver.repository;

import com.sparta.secureschedulerappserver.entity.Schedule;
import java.util.List;

public interface ScheduleRepositoryQuery {
    List<Schedule> searchByTitle(String title);

    List<Schedule> getSchedulesForUser(Long userId);
}
