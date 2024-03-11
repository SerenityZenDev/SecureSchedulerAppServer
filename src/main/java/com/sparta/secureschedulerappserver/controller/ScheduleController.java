package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.PageDto;
import com.sparta.secureschedulerappserver.dto.ScheduleListResponseDto;
import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.ScheduleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor

public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;

    @PostMapping("")
    @Operation(summary = "Create a new schedule", description = "Create a new schedule with the given details")
    public ScheduleResponseDto createSchedule(
        @Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.createSchedule(scheduleRequestDto, userDetails);
    }

    @GetMapping("/{scheduleId}")
    @Operation(summary = "Get a schedule by ID", description = "Returns a single schedule")
    public ScheduleResponseDto readSchedule(@PathVariable Long scheduleId) {
        return scheduleService.readSchedule(scheduleId);
    }

    @GetMapping("")
    @Operation(summary = "Get all schedules", description = "Returns a list of all schedules")
    public ScheduleListResponseDto readSchedules() {
        return scheduleService.readSchedules();
    }

    @GetMapping("/mySchedules")
    @Operation(summary = "Get current user's schedules", description = "Returns a list of schedules for the current user")
    public List<ScheduleResponseDto> readMySchedules(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PageDto pageDto = PageDto.builder().currentPage(1).size(1).build();
        return scheduleService.getSchedulesForUser(userDetails, pageDto);
    }

    @GetMapping("/uncompleted")
    @Operation(summary = "Get uncompleted schedules", description = "Returns a list of uncompleted schedules")
    public ScheduleListResponseDto readUncompleteSchedules() {
        return scheduleService.readUncompleteSchedules();
    }

    @GetMapping("/search")
    @Operation(summary = "Search schedules", description = "Search schedules by text")
    public List<ScheduleResponseDto> findSchedule(@RequestParam String text) {
        PageDto pageDto = PageDto.builder().currentPage(1).size(100).build();
        return scheduleService.showSchedules(text, pageDto);
    }

    @PatchMapping("/{scheduleId}")
    @Operation(summary = "Update a schedule", description = "Updates the schedule with the given ID")
    public ScheduleResponseDto updateSchedule(@PathVariable Long scheduleId,
        @Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.updateSchedule(scheduleId, scheduleRequestDto, userDetails);
    }

    @PatchMapping("/{scheduleId}/complete")
    @Operation(summary = "Mark a schedule as completed", description = "Marks the schedule with the given ID as completed")
    public ScheduleResponseDto completeSchedule( @PathVariable Long scheduleId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.completeSchedule(scheduleId, userDetails);
    }

    @PatchMapping("/{scheduleId}/hide")
    @Operation(summary = "Hide a schedule", description = "Hides the schedule with the given ID")
    public ScheduleResponseDto hideSchedule( @PathVariable Long scheduleId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.hideSchedule(scheduleId, userDetails);
    }
}

