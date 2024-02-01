package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.ScheduleListResponseDto;
import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ScheduleResponseDto createSchedule(
        @Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(scheduleRequestDto,
            userDetails);
        return scheduleResponseDto;
    }

    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto readSchedule(@PathVariable Long scheduleId) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.readSchedule(scheduleId);
        return scheduleResponseDto;
    }

    @GetMapping("")
    public ScheduleListResponseDto readSchedule() {
        ScheduleListResponseDto scheduleListResponseDto = scheduleService.readSchedules();
        return scheduleListResponseDto;
    }

    @PatchMapping("/{scheduleId}")
    public ScheduleResponseDto updateSchedule(@PathVariable Long scheduleId,
        @Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.updateSchedule(scheduleId,
            scheduleRequestDto, userDetails);
        return scheduleResponseDto;
    }

    @PatchMapping("/{scheduleId}/complete")
    public ScheduleResponseDto completeSchedule(@PathVariable Long scheduleId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.completeSchedule(scheduleId,
            userDetails);
        return scheduleResponseDto;
    }
}
