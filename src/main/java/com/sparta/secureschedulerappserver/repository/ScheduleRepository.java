package com.sparta.secureschedulerappserver.repository;

import com.sparta.secureschedulerappserver.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
