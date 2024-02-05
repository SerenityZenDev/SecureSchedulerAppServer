package com.sparta.secureschedulerappserver.service;

import com.sparta.secureschedulerappserver.dto.ScheduleListResponseDto;
import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.NotFoundScheduleException;
import com.sparta.secureschedulerappserver.exception.NotFoundUserException;
import com.sparta.secureschedulerappserver.exception.UnauthorizedOperationException;
import com.sparta.secureschedulerappserver.repository.ScheduleRepository;
import com.sparta.secureschedulerappserver.repository.UserRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto,
        UserDetailsImpl userDetails) {
        String title = scheduleRequestDto.getTitle();
        String content = scheduleRequestDto.getContent();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(
            NotFoundUserException::new
        );

        Schedule schedule = new Schedule(title, content, user);

        scheduleRepository.save(schedule);

        return new ScheduleResponseDto(schedule, username);
    }

    public ScheduleResponseDto readSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            NotFoundScheduleException::new
        );
        return new ScheduleResponseDto(schedule);
    }

    public ScheduleListResponseDto readSchedules() {
        List<User> users = userRepository.findAll();
        Map<String, List<ScheduleResponseDto>> scheduleByName = new HashMap<>();

        for (User user : users) {
            List<Schedule> schedules = scheduleRepository.findByUser_UserId(user.getUserId());
            if (schedules.isEmpty()) {
                continue;
            }

            List<ScheduleResponseDto> scheduleResponseDtos = schedules.stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());

            scheduleByName.put(user.getUsername(), scheduleResponseDtos);
        }

        return new ScheduleListResponseDto(scheduleByName);
    }

    public ScheduleListResponseDto readUncompleteSchedules() {
        List<User> users = userRepository.findAll();
        Map<String, List<ScheduleResponseDto>> scheduleByName = new HashMap<>();

        for (User user : users) {
            List<Schedule> schedules = scheduleRepository.findByUser_UserIdAndIsCompletedFalse(user.getUserId());
            if (schedules.isEmpty()) {
                continue;
            }

            List<ScheduleResponseDto> scheduleResponseDtos = schedules.stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());

            scheduleByName.put(user.getUsername(), scheduleResponseDtos);
        }

        return new ScheduleListResponseDto(scheduleByName);
    }

    public List<ScheduleResponseDto> findSchedules(String text) {
        List<Schedule> schedules = scheduleRepository.findAllByTitleContaining(text);
        List<ScheduleResponseDto> scheduleResponseDtos = new ArrayList<>();

        for (Schedule schedule : schedules) {
            scheduleResponseDtos.add(new ScheduleResponseDto(schedule));
        }

        return scheduleResponseDtos;
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId,
        ScheduleRequestDto scheduleRequestDto, UserDetailsImpl userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
            NotFoundUserException::new
        );
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            NotFoundScheduleException::new
        );
        if (!user.getUsername().equals(schedule.getUser().getUsername())) {
            throw new UnauthorizedOperationException();
        }

        schedule.update(scheduleRequestDto);

        return new ScheduleResponseDto(schedule,
            user.getUsername());
    }

    @Transactional
    public ScheduleResponseDto completeSchedule(Long scheduleId, UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
            NotFoundUserException::new
        );
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            NotFoundScheduleException::new
        );
        if (!user.getUsername().equals(schedule.getUser().getUsername())) {
            throw new UnauthorizedOperationException();
        }

        schedule.complete();

        return new ScheduleResponseDto(schedule,
            user.getUsername());
    }


}
