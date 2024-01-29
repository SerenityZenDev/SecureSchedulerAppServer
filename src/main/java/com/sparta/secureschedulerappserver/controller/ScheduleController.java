package com.sparta.secureschedulerappserver.controller;

import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduels")
@RequiredArgsConstructor
public class ScheduleController {
    private ScheduleService scheduleService;

    @PostMapping("")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto){
        return null;
    }

    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto readSchedule(@PathVariable int scheduleId){
        return null;
    }

    @GetMapping("")
    public ScheduleResponseDto readSchedule(){
        return null;
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
