package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.PageDto;
import com.sparta.secureschedulerappserver.dto.ScheduleListResponseDto;
import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.ScheduleServiceImpl;
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
    public ScheduleResponseDto createSchedule(
        @Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.createSchedule(scheduleRequestDto,
            userDetails);
    }

    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto readSchedule(@PathVariable Long scheduleId) {
        return scheduleService.readSchedule(scheduleId);
    }

    @GetMapping("")
    public ScheduleListResponseDto readSchedule() {
        return scheduleService.readSchedules();
    }

    @GetMapping("/mySchedules")
    public List<ScheduleResponseDto> readMySchedules(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PageDto pageDto = PageDto.builder().currentPage(1).size(100).build();
        return scheduleService.getSchedulesForUser(userDetails, pageDto);
    }


    @GetMapping("/uncompleted")
    public ScheduleListResponseDto readUncompleteSchedule() {
        return scheduleService.readUncompleteSchedules();
    }

    @GetMapping("/search")
    public List<ScheduleResponseDto> findSchedule(@RequestParam String text) {
        // 추후 입력을 받거나 하도록 수정
        PageDto pageDto = PageDto.builder().currentPage(1).size(100).build();
        return scheduleService.showSchedules(text, pageDto);
    }


    @PatchMapping("/{scheduleId}")
    public ScheduleResponseDto updateSchedule(@PathVariable Long scheduleId,
        @Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.updateSchedule(scheduleId, scheduleRequestDto, userDetails);
    }

    @PatchMapping("/{scheduleId}/complete")
    public ScheduleResponseDto completeSchedule(@PathVariable Long scheduleId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.completeSchedule(scheduleId, userDetails);
    }

    @PatchMapping("/{scheduleId}/hide")
    public ScheduleResponseDto hideSchedule(@PathVariable Long scheduleId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.hideSchedule(scheduleId, userDetails);
    }
}
