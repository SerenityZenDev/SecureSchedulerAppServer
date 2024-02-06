package com.sparta.secureschedulerappserver.dto;

import com.sparta.secureschedulerappserver.entity.Schedule;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter

public class ScheduleResponseDto {

    private String title;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private boolean isCompleted;
    private boolean hidden;

    public ScheduleResponseDto(Schedule schedule, String username) {
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.username = username;
        this.createAt = schedule.getCreateAt();
        this.isCompleted = schedule.isCompleted();
        this.hidden = schedule.isHidden();
    }

    public ScheduleResponseDto(Schedule schedule) {
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.username = schedule.getUser().getUsername();
        this.createAt = schedule.getCreateAt();
        this.isCompleted = schedule.isCompleted();
        this.hidden = schedule.isHidden();
    }

    public ScheduleResponseDto(ScheduleResponseDto scheduleResponseDto) {
        this.title = scheduleResponseDto.getTitle();
        this.content = scheduleResponseDto.getContent();
        this.username = scheduleResponseDto.getUsername();
        this.createAt = scheduleResponseDto.getCreateAt();
        this.isCompleted = scheduleResponseDto.isCompleted();
        this.hidden = scheduleResponseDto.isHidden();
    }
}
