package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.repository.ScheduleRepository;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto, UserDetailsImpl userDetails) {
        String title = scheduleRequestDto.getTitle();
        String content = scheduleRequestDto.getContent();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow();

        Schedule schedule = new Schedule(title, content, user);

        scheduleRepository.save(schedule);

        return new ScheduleResponseDto(schedule, username);
    }

    public ScheduleResponseDto readSchedule(Long scheduleId, UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Schedule> schedules = scheduleRepository.findByUser_UserId(user.getUserId());
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedules.get(
            (int) (scheduleId-1)), user.getUsername());

        return scheduleResponseDto;

    }

    public List<ScheduleResponseDto> readSchedules(UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Schedule> schedules = scheduleRepository.findByUser_UserId(user.getUserId());
        List<ScheduleResponseDto> scheduleResponseDtos = new ArrayList<>();
        for (Schedule schedule : schedules){
            scheduleResponseDtos.add(new ScheduleResponseDto(schedule, userDetails.getUsername()));
        }
        return scheduleResponseDtos;

    }


    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId,
        ScheduleRequestDto scheduleRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Schedule schedule = scheduleRepository.findByUser_UserId(user.getUserId()).get(
            (int) (scheduleId-1));

        schedule.update(scheduleRequestDto);
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule, user.getUsername());

        return scheduleResponseDto;
    }

    @Transactional
    public ScheduleResponseDto completeSchedule(Long scheduleId, UserDetailsImpl userDetails) {
            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            Schedule schedule = scheduleRepository.findByUser_UserId(user.getUserId()).get(
                (int) (scheduleId-1));

            schedule.complete();
            ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule, user.getUsername());

            return scheduleResponseDto;
    }
}
