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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto,
        UserDetailsImpl userDetails) {
        User user = findUserByUsername(userDetails.getUsername());
        Schedule schedule = new Schedule(scheduleRequestDto.getTitle(),
            scheduleRequestDto.getContent(), user);
        scheduleRepository.save(schedule);
        return new ScheduleResponseDto(schedule, user.getUsername());
    }

    @Override
    public ScheduleResponseDto readSchedule(Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public ScheduleListResponseDto readSchedules() {
        List<User> users = userRepository.findAll();
        Map<String, List<ScheduleResponseDto>> scheduleByName = new HashMap<>();
        users.forEach(user -> {
            List<Schedule> schedules = scheduleRepository.findByUser_UserIdAndHiddenFalse(
                user.getUserId());
            if (!schedules.isEmpty()) {
                List<ScheduleResponseDto> scheduleResponseDtos = schedules.stream()
                    .map(ScheduleResponseDto::new)
                    .collect(Collectors.toList());
                scheduleByName.put(user.getUsername(), scheduleResponseDtos);
            }
        });
        return new ScheduleListResponseDto(scheduleByName);
    }

    @Override
    public ScheduleListResponseDto readMySchedules(UserDetailsImpl userDetails) {
        User user = findUserByUsername(userDetails.getUsername());
        List<Schedule> schedules = scheduleRepository.findByUser_UserId(user.getUserId());
        List<ScheduleResponseDto> scheduleResponseDtos = schedules.stream()
            .map(ScheduleResponseDto::new)
            .collect(Collectors.toList());
        Map<String, List<ScheduleResponseDto>> scheduleByName = new HashMap<>();
        scheduleByName.put(user.getUsername(), scheduleResponseDtos);
        return new ScheduleListResponseDto(scheduleByName);
    }

    @Override
    public ScheduleListResponseDto readUncompleteSchedules() {
        List<User> users = userRepository.findAll();
        Map<String, List<ScheduleResponseDto>> scheduleByName = new HashMap<>();
        users.forEach(user -> {
            List<Schedule> schedules = scheduleRepository.findByUser_UserIdAndIsCompletedFalseAndHiddenFalse(
                user.getUserId());
            if (!schedules.isEmpty()) {
                List<ScheduleResponseDto> scheduleResponseDtos = schedules.stream()
                    .map(ScheduleResponseDto::new)
                    .collect(Collectors.toList());
                scheduleByName.put(user.getUsername(), scheduleResponseDtos);
            }
        });
        return new ScheduleListResponseDto(scheduleByName);
    }

    @Override
    public List<ScheduleResponseDto> findSchedules(String text) {
        List<Schedule> schedules = scheduleRepository.findAllByTitleContainingAndHiddenFalse(text);
        return schedules.stream()
            .map(ScheduleResponseDto::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId,
        ScheduleRequestDto scheduleRequestDto, UserDetailsImpl userDetails) {
        User user = findUserByUsername(userDetails.getUsername());
        Schedule schedule = findScheduleById(scheduleId);
        validateUserAccess(user, schedule);
        schedule.update(scheduleRequestDto.getTitle(), scheduleRequestDto.getContent());
        return new ScheduleResponseDto(schedule, user.getUsername());
    }

    @Override
    @Transactional
    public ScheduleResponseDto completeSchedule(Long scheduleId, UserDetailsImpl userDetails) {
        User user = findUserByUsername(userDetails.getUsername());
        Schedule schedule = findScheduleById(scheduleId);
        validateUserAccess(user, schedule);
        schedule.complete();
        return new ScheduleResponseDto(schedule, user.getUsername());
    }

    @Override
    @Transactional
    public ScheduleResponseDto hideSchedule(Long scheduleId, UserDetailsImpl userDetails) {
        User user = findUserByUsername(userDetails.getUsername());
        Schedule schedule = findScheduleById(scheduleId);
        validateUserAccess(user, schedule);
        schedule.optionHidden();
        return new ScheduleResponseDto(schedule, user.getUsername());
    }

    @Override
    public List<ScheduleResponseDto> showSchedules(String keyword) {
        return scheduleRepository.searchByTitle(keyword)
            .stream()
            .map(ScheduleResponseDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponseDto> getSchedulesForUser(UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getUserId()).orElseThrow(
            NotFoundUserException::new
        );

        return scheduleRepository.getSchedulesForUser(user.getUserId())
            .stream()
            .map(ScheduleResponseDto::new)
            .collect(Collectors.toList());
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(NotFoundScheduleException::new);
    }

    private void validateUserAccess(User user, Schedule schedule) {
        if (!user.getUsername().equals(schedule.getUser().getUsername())) {
            throw new UnauthorizedOperationException();
        }
    }
}
