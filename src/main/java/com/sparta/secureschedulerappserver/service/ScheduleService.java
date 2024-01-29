package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private ScheduleRepository scheduleRepository;
}
