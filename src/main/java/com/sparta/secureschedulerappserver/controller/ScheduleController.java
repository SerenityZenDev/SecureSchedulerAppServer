package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ScheduleResponseDto scheduleResponseDto =  scheduleService.createSchedule(scheduleRequestDto, userDetails);
        return scheduleResponseDto;
    }

    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto readSchedule(@PathVariable Long scheduleId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ScheduleResponseDto scheduleResponseDto = scheduleService.readSchedule(scheduleId, userDetails);
        return scheduleResponseDto;
    }

    @GetMapping("")
    public List<ScheduleResponseDto> readSchedule(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<ScheduleResponseDto> scheduleResponseDtos = scheduleService.readSchedules(userDetails);
        return scheduleResponseDtos;
    }

    @PatchMapping("/{scheduleId}")
    public ScheduleResponseDto updateSchedule(){
        return null;
    }

    @DeleteMapping("/{scheduleId}")
    public ScheduleResponseDto deleteSchedule(){
        return null;
    }
}
